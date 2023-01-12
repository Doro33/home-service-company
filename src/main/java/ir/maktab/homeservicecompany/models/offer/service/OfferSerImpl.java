package ir.maktab.homeservicecompany.models.offer.service;

import ir.maktab.homeservicecompany.models.offer.dto.OfferDTO;
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

import java.time.Duration;
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
    public Offer saveNewOffer(OfferDTO offerDTO) {
        Worker worker = workerValidation(offerDTO.getWorkerId());
        Request request = requestValidation(offerDTO.getRequestId());
        offerValidate(offerDTO, worker, request);

        Offer offer = new Offer(worker, request,
                offerDTO.getExpectedPrice(),
                Duration.ofHours(offerDTO.getExpectedDurationByHour()));

        request.setStatus(RequestStatus.AWAITING_FOR_CHOOSE_A_WORKER);
        requestSer.saveOrUpdate(request);

        worker.setOfferCounter(worker.getOfferCounter()+1);
        workerSer.saveOrUpdate(worker);

        return saveOrUpdate(offer);
    }

    private void offerValidate(OfferDTO offerDTO, Worker worker, Request request) {
        RequestStatus status = request.getStatus();
        Job job = request.getJob();
        Double minPrice = job.getMinimumPrice();
        if (existsByWorkerAndRequest(worker, request))
            throw new IllegalArgumentException("this worker already offered for this request.");
        if (!workerSkillSer.canWorkerDoThisJob(worker, job))
            throw new SkillsException("the worker doesnt have skill for this job");
        if (!(status == RequestStatus.AWAITING_FOR_OFFERS
                || status == RequestStatus.AWAITING_FOR_CHOOSE_A_WORKER))
            throw new RequestStatusException("this request has been taken.");
        if (offerDTO.getExpectedPrice() < minPrice)
            throw new IllegalArgumentException("expected price cannot be lesser than " + minPrice);
        if (offerDTO.getExpectedDurationByHour() < 1)
            throw new IllegalArgumentException("duration must be at least 1 hour.");
    }

    @Override
    public List<Offer> findByRequestOrderByExpectedPrice(Request request) {
        requestValidation(request.getId());
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

    @Override
    public boolean existsByWorkerAndRequest(Worker worker, Request request) {
        return repository.existsByWorkerAndRequest(worker,request);
    }

    private Worker workerValidation(Long workerId) {
        if (workerId == null)
            throw new NullIdException("worker's id cannot be null.");
        Worker worker = workerSer.findById(workerId);
        if (worker == null)
            throw new IllegalArgumentException("worker's id is not valid.");
        return worker;
    }

    private Request requestValidation(Long requestId) {
        if (requestId == null)
            throw new NullIdException("request's id cannot be null.");
        Request request = requestSer.findById(requestId);
        if (request == null)
            throw new IllegalArgumentException("request's id is not valid.");
        return request;
    }
}