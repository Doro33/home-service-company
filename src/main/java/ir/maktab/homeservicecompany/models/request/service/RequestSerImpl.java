package ir.maktab.homeservicecompany.models.request.service;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.request.dto.FilterRequestDTO;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.request.dao.RequestDao;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestSerImpl extends BaseServiceImpl<Request, RequestDao> implements RequestService {
    public RequestSerImpl(RequestDao repository, ClientService clientSer, JobService jobSer, Validation validation) {
        super(repository);
        this.clientSer = clientSer;
        this.jobSer = jobSer;
        this.validation = validation;
    }

    private final ClientService clientSer;
    private final JobService jobSer;
    private final Validation validation;
    @PersistenceContext
    private EntityManager em;


    @Override
    public void saveNewRequest(RequestDTO requestDTO) {
        Client client = validation.clientValidate(requestDTO.getClientId());
        Job job = validation.jobValidate(requestDTO.getJobId());
        Request request = requestMaker(requestDTO, client, job);

        client.setRequestCounter(
                client.getRequestCounter() + 1
        );
        clientSer.saveOrUpdate(client);
        saveOrUpdate(request);
    }

    @Override
    public List<Request> findByJob(Long jobId) {
        Job job = jobSer.findById(jobId);
        return repository.findByJobAndAcceptedOfferIsNullOrderByDate(job);
    }

    @Override
    public List<Request> findByClient(Long clientId) {
        Client client = validation.clientValidate(clientId);
        return repository.findByClient(client);
    }

    @Override
    public List<Request> requestCriteria(FilterRequestDTO filterRequestDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Request> query = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = query.from(Request.class);
        predicateMaker(filterRequestDTO, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    private static Request requestMaker(RequestDTO requestDTO, Client client, Job job) {
        if (requestDTO.getProposedPrice() <= job.getMinimumPrice())
            throw new IllegalArgumentException("propose price must be at least." + job.getMinimumPrice());
        if (requestDTO.getDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("past date cannot be chosen.");
        if (requestDTO.getDate().isEqual(LocalDate.now()) &&
                requestDTO.getSuggestedTime().isBefore(LocalTime.now().plusMinutes(30)))
            throw new IllegalArgumentException("request's suggested time must be at least 30 minuets later from now.");
        return new Request(client, job,
                requestDTO.getProposedPrice(),
                requestDTO.getDescription(),
                requestDTO.getDate(),
                requestDTO.getSuggestedTime(),
                requestDTO.getAddress());
    }

    private void predicateMaker(FilterRequestDTO filterRequestDTO, List<Predicate> predicateList,
                                CriteriaBuilder criteriaBuilder, Root<Request> root) {
        Long categoryId = filterRequestDTO.getCategoryId();
        Long jobId = filterRequestDTO.getJobId();
        RequestStatus status = filterRequestDTO.getRequestStatus();
        jobCategoryValidate(categoryId, jobId);
        if (categoryId != null) {
            validation.categoryValidate(categoryId);
            predicateList.add(criteriaBuilder.equal(root.get("category.id"), categoryId));
        }
        if (jobId != null) {
            validation.jobValidate(jobId);
            predicateList.add(criteriaBuilder.equal(root.get("job.id"), categoryId));
        }
        if (status != null) {
            predicateList.add(criteriaBuilder.equal(root.get("status"), status));
        }
        requestDatePredicateMaker(filterRequestDTO, predicateList, criteriaBuilder, root);
    }

    private static void requestDatePredicateMaker(FilterRequestDTO filterRequestDTO,
                                                  List<Predicate> predicateList, CriteriaBuilder criteriaBuilder,
                                                  Root<Request> root) {
        LocalDate requestedAfter = filterRequestDTO.getRequestedAfter();
        LocalDate requestedBefore = filterRequestDTO.getRequestedBefore();
        if (requestedAfter == null)
            requestedAfter = LocalDate.MIN;
        if (requestedBefore == null)
            requestedBefore = LocalDate.now();
        if (requestedBefore.isBefore(requestedAfter))
            throw new IllegalArgumentException("requestedBefore date cannot be before requestedAfter date");
        predicateList.add(criteriaBuilder.between(root.get("date"), requestedAfter, requestedBefore));
    }

    private void jobCategoryValidate(Long categoryId, Long jobId) {
        if (jobId != null && categoryId != null) {
            Category category = validation.categoryValidate(categoryId);
            Job job = validation.jobValidate(jobId);
            if (job.getCategory() != category)
                throw new IllegalArgumentException("this job does not belong to this category. ");
        }
    }
}