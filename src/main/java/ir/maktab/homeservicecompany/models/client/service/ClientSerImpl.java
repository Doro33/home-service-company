package ir.maktab.homeservicecompany.models.client.service;

import com.google.common.base.Strings;
import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.models.admin.service.AdminService;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.dao.ClientDao;
import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.utils.exception.CreditAmountException;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
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


@Service
public class ClientSerImpl extends BaseServiceImpl<Client, ClientDao> implements ClientService {
    public ClientSerImpl(ClientDao repository, @Lazy RequestService requestSer, OfferService offerSer, WorkerService workerSer, @Lazy AdminService adminSer, Validation validation) {
        super(repository);

        this.requestSer = requestSer;
        this.offerSer = offerSer;
        this.workerSer = workerSer;
        this.adminSer = adminSer;
        this.validation = validation;
    }

    private final RequestService requestSer;
    private final OfferService offerSer;
    private final WorkerService workerSer;

    private final AdminService adminSer;
    private final Validation validation;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Client findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public Client signUp(Client client) {
        if (client.getId() != null)
            throw new InvalidIdException("new client's id must be null.");
        validation.nameValidate(
                client.getFirstName(), client.getLastName()
        );
        if (findByEmail(client.getEmail()) != null)
            throw new IllegalArgumentException("this email has been used.");
        validation.passwordValidate(client.getPassword());
        return saveOrUpdate(client);
    }

    @Override
    public Client changePassword(String email, String oldPassword, String newPassword1, String newPassword2) {
        Client client = findByEmail(email);
        if (client == null)
            throw new IllegalArgumentException("this email does not have an account.");
        if (!oldPassword.equals(client.getPassword()))
            throw new IllegalArgumentException("incorrect password");
        if (!newPassword2.matches(newPassword1))
            throw new IllegalArgumentException("new passwords are not match.");
        validation.passwordValidate(newPassword1);

        client.setPassword(newPassword1);
        return saveOrUpdate(client);
    }
    @Override
    public void setRequestStatusOnStarted(Long clientId, Long requestId) {
        Client client = clientValidation(clientId);
        Request request = requestValidation(requestId);
        if (request.getClient()!=client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
        if (request.getStatus() != RequestStatus.WORKER_ON_THE_ROAD)
            throw new RequestStatusException("incorrect request's status for this function.");
        request.setStatus(RequestStatus.STARTED);
        request.setStartAt(LocalTime.now());
        requestSer.saveOrUpdate(request);
    }

    @Override
    @Transactional
    public void setRequestStatusOnCompleted(Long clientId, Long requestId) {
        Client client = clientValidation(clientId);
        Request request = requestValidation(requestId);
        if (request.getClient()!=client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
        if (request.getStatus() != RequestStatus.STARTED)
            throw new RequestStatusException("incorrect request's status for this function.");
        request.setStatus(RequestStatus.COMPLETED);
        request.setEndAt(LocalTime.now());
        Long extraHours = extraHoursCalculator(request);
        if (extraHours > 0) {
            Worker worker = request.getAcceptedOffer().getWorker();
            worker.extraHourPenalty(extraHours);
            workerSer.saveOrUpdate(worker);
        }
        requestSer.saveOrUpdate(request);
    }
    @Override
    @Transactional
    public void chooseAnOffer(ChooseOfferDTO chooseOfferDTO) {
        Client client = clientValidation(chooseOfferDTO.getClientId());
        Request request = requestValidation(chooseOfferDTO.getRequestId());
        Offer offer = offerValidation(chooseOfferDTO.getOfferId());
        if (request.getClient() != client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
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
        Client client = clientValidation(clientId);
        Request request = requestValidation(requestId);
        Offer acceptedOffer = request.getAcceptedOffer();
        Double clientCredit = client.getCredit();
        Double workPrice = acceptedOffer.getExpectedPrice();
        Worker worker = acceptedOffer.getWorker();
        Admin admin = adminSer.findById(1L);

        if (request.getClient() != client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
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
    public List<Client> clientCriteria(ClientDTO clientDto) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);
        predicateMaker(clientDto, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    private Client clientValidation(Long clientId) {
        if (clientId == null)
            throw new NullIdException("client's id cannot be null.");
        Client client = findById(clientId);
        if (client == null)
            throw new IllegalArgumentException("client's id is not valid.");
        return client;
    }

    private Request requestValidation(Long requestId) {
        if (requestId == null)
            throw new NullIdException("request's id cannot be null.");
        Request request = requestSer.findById(requestId);
        if (request == null)
            throw new IllegalArgumentException("request's id is not valid.");
        return request;
    }

    private Offer offerValidation(Long offerId) {
        if (offerId == null)
            throw new NullIdException("offer's id cannot be null.");
        Offer offer = offerSer.findById(offerId);
        if (offer == null)
            throw new IllegalArgumentException("offer's id is not valid.");
        return offer;
    }

    private static Long extraHoursCalculator(Request request) {
        Duration actualDuration = Duration.between(request.getStartAt(), request.getEndAt());
        Duration expectedDuration = request.getAcceptedOffer().getExpectedDuration();
        Duration extraDuration = actualDuration.minus(expectedDuration);
        return extraDuration.toHours();
    }

    private void predicateMaker(ClientDTO clientDto, List<Predicate> predicateList,
                                CriteriaBuilder criteriaBuilder, Root<Client> root) {
        String firstName = clientDto.getFirstName();
        String lastName = clientDto.getLastName();
        String email = clientDto.getEmail();

        if (!Strings.isNullOrEmpty(firstName)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("firstname"), sampleMaker(firstName)));
        }
        if (!Strings.isNullOrEmpty(lastName)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("lastname"), sampleMaker(lastName)));
        }
        if (Strings.isNullOrEmpty(email)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("email"), sampleMaker(email)));
        }
    }

    private String sampleMaker(String input) {
        return "%" + input + "%";
    }
}