package ir.maktab.homeservicecompany.models.offer.service;

import ir.maktab.homeservicecompany.models.offer.dto.OfferDTO;
import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.utils.exception.RequestStatusException;
import ir.maktab.homeservicecompany.utils.exception.SkillsException;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.offer.dao.OfferDao;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.service.WorkerSkillService;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class OfferSerImpl extends BaseServiceImpl<Offer, OfferDao> implements OfferService {
    public OfferSerImpl(OfferDao repository, WorkerService workerSer, @Lazy RequestService requestSer,
                        WorkerSkillService workerSkillSer, Validation validation) {
        super(repository);
        this.workerSer = workerSer;
        this.requestSer = requestSer;
        this.workerSkillSer = workerSkillSer;
        this.validation = validation;
    }

    private final WorkerService workerSer;
    private final RequestService requestSer;
    private final WorkerSkillService workerSkillSer;
    private final Validation validation;

    @Override
    @Transactional
    public void saveNewOffer(OfferDTO offerDTO) {
        Worker worker = validation.workerValidate(offerDTO.getWorkerId());
        Request request = validation.requestValidate(offerDTO.getRequestId());
        offerCustomValidate(offerDTO, worker, request);

        Offer offer = new Offer(worker, request,
                offerDTO.getExpectedPrice(),
                Duration.ofHours(offerDTO.getExpectedDurationByHour()));

        request.setStatus(RequestStatus.AWAITING_FOR_CHOOSE_A_WORKER);
        requestSer.saveOrUpdate(request);

        worker.setOfferCounter(worker.getOfferCounter() + 1);
        workerSer.saveOrUpdate(worker);

        saveOrUpdate(offer);
    }

    @Override
    public List<Offer> findByRequestOrderByExpectedPrice(Long requestId) {
        Request request = validation.requestValidate(requestId);
        if (request.getAcceptedOffer()!=null)
            throw new IllegalArgumentException("this request already chose an offer");
        return repository.findByRequestOrderByExpectedPrice(requestId);
    }

    @Override
    public List<Offer> findByRequestOrderByWorkerScore(Long requestId) {
        Request request = validation.requestValidate(requestId);
        if (request.getAcceptedOffer()!=null)
            throw new IllegalArgumentException("this request already chose an offer");
        return repository.findByRequestOrderByWorkerScore(requestId);
    }

    @Override
    public List<Offer> findByWorker(Long workerId) {
        Worker worker = validation.workerValidate(workerId);
        return repository.findByWorker(worker);
    }

    @Override
    public boolean existsByWorkerAndRequest(Worker worker, Request request) {
        return repository.existsByWorkerAndRequest(worker, request);
    }
    private void offerCustomValidate(OfferDTO offerDTO, Worker worker, Request request) {
        Job job = request.getJob();
        Double minPrice = job.getMinimumPrice();
        if (existsByWorkerAndRequest(worker, request))
            throw new IllegalArgumentException("this worker already offered for this request.");
        if (!workerSkillSer.canWorkerDoThisJob(worker, job))
            throw new SkillsException("the worker doesnt have skill for this job");
        if (request.getAcceptedOffer()!=null)
            throw new RequestStatusException("this request has been taken.");
        if (offerDTO.getExpectedPrice() < minPrice)
            throw new IllegalArgumentException("expected price cannot be lesser than " + minPrice);
        if (offerDTO.getExpectedDurationByHour() < 1)
            throw new IllegalArgumentException("duration must be at least 1 hour.");
    }
}