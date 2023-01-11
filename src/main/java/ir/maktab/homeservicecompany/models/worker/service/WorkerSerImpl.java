package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.worker.dao.WorkerDao;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkerSerImpl extends BaseServiceImpl<Worker, WorkerDao> implements WorkerService {
    @PersistenceContext
    private EntityManager em;

    public WorkerSerImpl(WorkerDao repository, @Lazy OfferService offerSer, Validation validation) {
        super(repository);
        this.offerSer = offerSer;
        this.validation = validation;
    }

    private final OfferService offerSer;
    private final Validation validation;

    @Override
    public Worker findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public Worker changePassword(String email, String oldPassword, String newPassword1, String newPassword2) {
        Worker worker = findByEmail(email);
        if (worker == null)
            throw new IllegalArgumentException("this email does not have an account.");
        if (!oldPassword.equals(worker.getPassword()))
            throw new IllegalArgumentException("incorrect password");
        if (!newPassword2.matches(newPassword1))
            throw new IllegalArgumentException("new passwords are not match.");
        validation.passwordValidate(newPassword1);

        worker.setPassword(newPassword1);
        return saveOrUpdate(worker);
    }

    @Override
    public Worker signUp(Worker worker) {
        if (worker.getId() != null) throw new InvalidIdException("new worker's id must be null.");
        validation.nameValidate(worker.getFirstName(), worker.getLastName());
        if (findByEmail(worker.getEmail()) != null) throw new IllegalArgumentException("this email has been used.");
        return saveOrUpdate(worker);
    }

    @Override
    public Offer addOffer(Offer offer) {
        return offerSer.saveNewOffer(offer);
    }

    @Override
    public List<Worker> workerCriteria(WorkerDto workerDto) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Worker> query = criteriaBuilder.createQuery(Worker.class);
        Root<Worker> root = query.from(Worker.class);
        createPredicates(workerDto, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    private void createPredicates(WorkerDto workerDto, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Worker> root) {
        Long minScore = workerDto.getMinScore();
        Long maxScore = workerDto.getMaxScore();
        if (minScore == null && maxScore != null) {
            predicateList.add(criteriaBuilder.between(root.get("score"), 0L, maxScore));
        } else if (minScore != null && maxScore == null) {
            predicateList.add(criteriaBuilder.between(root.get("score"), minScore, Long.MAX_VALUE));
        } else if (minScore != null) {
            predicateList.add(criteriaBuilder.between(root.get("score"), minScore, maxScore));
        }
    }


}