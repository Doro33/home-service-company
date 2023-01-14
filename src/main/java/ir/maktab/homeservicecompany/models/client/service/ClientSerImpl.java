package ir.maktab.homeservicecompany.models.client.service;

import com.google.common.base.Strings;
import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.models.admin.service.AdminService;
import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.bank_card.service.BankCardService;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.dao.ClientDao;
import ir.maktab.homeservicecompany.models.client.dto.FilterClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import ir.maktab.homeservicecompany.utils.dto.UserDTO;
import ir.maktab.homeservicecompany.utils.exception.CreditAmountException;
import ir.maktab.homeservicecompany.utils.exception.RequestStatusException;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ClientSerImpl extends BaseServiceImpl<Client, ClientDao> implements ClientService {
    public ClientSerImpl(ClientDao repository, @Lazy AdminService adminSer, WorkerService workerSer,
                         @Lazy RequestService requestSer, OfferService offerSer, BankCardService bankCardSer,
                         Validation validation) {
        super(repository);
        this.adminSer = adminSer;
        this.workerSer = workerSer;
        this.requestSer = requestSer;
        this.offerSer = offerSer;
        this.bankCardSer = bankCardSer;
        this.validation = validation;
    }

    private final AdminService adminSer;
    private final WorkerService workerSer;

    private final RequestService requestSer;
    private final OfferService offerSer;

    private final BankCardService bankCardSer;
    private final Validation validation;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Client> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void signUp(UserDTO userDTO) {
        String firstName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        validation.nameValidate(firstName, lastName);
        validation.passwordValidate(password);

        if (findByEmail(email).isPresent())
            throw new IllegalArgumentException("this email has been used.");

        Client client = new Client(firstName, lastName, email, password);
        saveOrUpdate(client);
    }

    @Override
    public void changePassword(PasswordDTO passwordDTO) {
        Client client = findByEmail(passwordDTO.getEmail()).
                orElseThrow(() -> new IllegalArgumentException("this email does not have an account."));
        String newPassword = validation.checkPasswords(passwordDTO, client.getPassword());

        client.setPassword(newPassword);
        saveOrUpdate(client);

        client.setPassword(newPassword);
        saveOrUpdate(client);
    }

    @Override
    public void setRequestStatusOnStarted(Long clientId, Long requestId) {
        Client client = validation.clientValidate(clientId);
        Request request = validation.requestValidate(requestId);
        checkRequestOwner(client, request);
        if (request.getStatus() != RequestStatus.WORKER_ON_THE_ROAD)
            throw new RequestStatusException("incorrect request's status for this function.");
        request.setStatus(RequestStatus.STARTED);
        request.setStartAt(LocalTime.now());
        requestSer.saveOrUpdate(request);
    }

    @Override
    @Transactional
    public void setRequestStatusOnCompleted(Long clientId, Long requestId) {
        Client client = validation.clientValidate(clientId);
        Request request = validation.requestValidate(requestId);
        Worker worker = request.getAcceptedOffer().getWorker();
        checkRequestOwner(client, request);
        if (request.getStatus() != RequestStatus.STARTED)
            throw new RequestStatusException("incorrect request's status for this function.");
        request.setStatus(RequestStatus.COMPLETED);
        request.setEndAt(LocalTime.now());
        Long extraHours = extraHoursCalculator(request);
        if (extraHours > 0) {
            worker.extraHourPenalty(extraHours);
        }
        worker.completedRequestEffect();
        workerSer.saveOrUpdate(worker);
        requestSer.saveOrUpdate(request);
    }

    @Override
    @Transactional
    public void chooseAnOffer(ChooseOfferDTO chooseOfferDTO) {
        Client client = validation.clientValidate(chooseOfferDTO.getClientId());
        Request request = validation.requestValidate(chooseOfferDTO.getRequestId());
        Offer offer = validation.offerValidate(chooseOfferDTO.getOfferId());
        checkRequestOwner(client, request);
        if (offer.getRequest() != request)
            throw new IllegalArgumentException("offer doesnt belong to this request");

        offer.setClientAccepted(true);
        offerSer.saveOrUpdate(offer);

        request.setAcceptedOffer(offer);
        request.setStatus(RequestStatus.WORKER_ON_THE_ROAD);
        requestSer.saveOrUpdate(request);
    }

    @Override
    @Transactional
    public void payWithCredit(Long clientId, Long requestId) {
        Client client = validation.clientValidate(clientId);
        Request request = validation.requestValidate(requestId);
        Offer acceptedOffer = request.getAcceptedOffer();
        Double clientCredit = client.getCredit();
        Double workPrice = acceptedOffer.getExpectedPrice();
        Worker worker = acceptedOffer.getWorker();
        Admin admin = adminSer.findById(1L);

        checkRequestOwner(client, request);
        if (request.getStatus() != RequestStatus.COMPLETED)
            throw new RequestStatusException("this request must be completed first.");

        if (clientCredit < workPrice)
            throw new CreditAmountException("client's credit is not enough.");

        client.setCredit(clientCredit - workPrice);
        saveOrUpdate(client);

        worker.setCredit(worker.getCredit() + workPrice * 0.7);
        workerSer.saveOrUpdate(worker);

        admin.setCredit(admin.getCredit() + workPrice * 0.3);
        adminSer.saveOrUpdate(admin);

        request.setStatus(RequestStatus.PAID);
        requestSer.saveOrUpdate(request);
    }

    @Override
    public List<Client> clientCriteria(FilterClientDTO filterClientDto) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);
        predicateMaker(filterClientDto, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public void increaseCredit(Long clientId, MoneyTransferDTO moneyTransferDTO) {
        Client client = validation.clientValidate(clientId);
        Double amount = moneyTransferDTO.getAmount();
        bankCardSer.moneyTransfer(moneyTransferDTO);

        client.setCredit(client.getCredit() + amount);
        saveOrUpdate(client);
    }
    private static void checkRequestOwner(Client client, Request request) {
        if (request.getClient() != client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
    }
    private static Long extraHoursCalculator(Request request) {
        Duration actualDuration = Duration.between(request.getStartAt(), request.getEndAt());
        Duration expectedDuration = request.getAcceptedOffer().getExpectedDuration();
        Duration extraDuration = actualDuration.minus(expectedDuration);
        return extraDuration.toHours();
    }

    private void predicateMaker(FilterClientDTO filterClientDto, List<Predicate> predicateList,
                                CriteriaBuilder criteriaBuilder, Root<Client> root) {
        String firstName = filterClientDto.getFirstName();
        String lastName = filterClientDto.getLastName();
        String email = filterClientDto.getEmail();

        if (!Strings.isNullOrEmpty(firstName)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("firstName"), sampleMaker(firstName)));
        }
        if (!Strings.isNullOrEmpty(lastName)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("lastName"), sampleMaker(lastName)));
        }
        if (Strings.isNullOrEmpty(email)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("email"), sampleMaker(email)));
        }
        requestNumberPredicateMaker(filterClientDto, predicateList, criteriaBuilder, root);
    }

    private static void requestNumberPredicateMaker(FilterClientDTO filterClientDto, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Client> root) {
        Integer minRequestNumber = filterClientDto.getMinRequestNumber();
        Integer maxRequestNumber = filterClientDto.getMaxRequestNumber();
        if (minRequestNumber == null)
            minRequestNumber = 0;
        if (maxRequestNumber == null)
            maxRequestNumber = Integer.MAX_VALUE;
        if (maxRequestNumber < minRequestNumber)
            throw new IllegalArgumentException("max requestNumber cannot be lesser than min requestNumber");
        if (minRequestNumber < 0)
            throw new IllegalArgumentException("min requestNumber cannot be lesser than 0.");
        predicateList.add(criteriaBuilder.
                between(root.get("requestCounter"), minRequestNumber, maxRequestNumber));
    }

    private String sampleMaker(String input) {
        return "%" + input + "%";
    }
}