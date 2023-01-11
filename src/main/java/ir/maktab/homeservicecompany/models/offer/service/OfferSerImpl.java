package ir.maktab.homeservicecompany.models.offer.service;

import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.utils.exception.RequestStatusException;
import ir.maktab.homeservicecompany.utils.exception.SkillsException;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.offer.dao.OfferDao;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.service.WorkerSkillService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferSerImpl extends BaseServiceImpl<Offer, OfferDao> implements OfferService {
    public OfferSerImpl(OfferDao repository, WorkerService workerSer, @Lazy RequestService requestSer, WorkerSkillService workerSkillSer) {
        super(repository);
        this.workerSer = workerSer;
        this.requestSer = requestSer;
        this.workerSkillSer = workerSkillSer;
    }

    private final WorkerService workerSer;
    private final RequestService requestSer;
    private final WorkerSkillService workerSkillSer;

    @Override
    @Transactional
    public Offer saveNewOffer(Offer offer) {
        Request offerRequest = offer.getRequest();
        if (offer.getId() != null)
            throw new InvalidIdException("new offer's id must be null.");
        workerValidation(offer.getWorker());
        requestValidation(offerRequest);
        workerPermit(offer.getWorker(), offerRequest);
        if (!(offerRequest.getStatus() == RequestStatus.AWAITING_FOR_OFFERS
                || offerRequest.getStatus() == RequestStatus.AWAITING_FOR_CHOOSE_A_WORKER))
            throw new RequestStatusException("this request has been taken.");
        if (offer.getExpectedPrice() < offerRequest.getJob().getMinimumPrice())
            throw new IllegalArgumentException("expected price cannot be lesser than minimum price of the job.");
        if (!offer.getExpectedDuration().isPositive())
            throw new IllegalArgumentException("duration must be positive.");

        offerRequest.setStatus(RequestStatus.AWAITING_FOR_CHOOSE_A_WORKER);
        requestSer.saveOrUpdate(offerRequest);
        return saveOrUpdate(offer);
    }

    @Override
    public List<Offer> findByRequestOrderByExpectedPrice(Request request) {
        requestValidation(request);
        return repository.findByRequestOrderByExpectedPrice(request);
    }

    @Override
    public List<Offer> findByRequestOrderByWorkerScore(Long requestId) {
        if (requestId == null)
            throw new NullIdException("request's id cannot be null.");
        if (requestSer.findById(requestId) == null)
            throw new IllegalArgumentException("request's id is not valid.");
        return repository.findByRequestOrderByWorkerScore(requestId);
    }

    private void workerValidation(Worker worker) {
        if (worker.getId() == null)
            throw new NullIdException("worker's id cannot be null.");
        if (workerSer.findById(worker.getId()) == null)
            throw new IllegalArgumentException("worker's id is not valid.");
    }

    private void requestValidation(Request request) {
        if (request.getId() == null)
            throw new NullIdException("request's id cannot be null.");
        if (requestSer.findById(request.getId()) == null)
            throw new IllegalArgumentException("request's id is not valid.");
    }

    private void workerPermit(Worker worker, Request request) {
        List<Job> workerSkills = workerSkillSer.findWorkerSkills(worker);
        if (!workerSkills.contains(request.getJob()))
            throw new SkillsException("the worker doesnt have skill for this job");
    }
}