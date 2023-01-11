package ir.maktab.homeservicecompany.models.request.service;

import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.request.dao.RequestDao;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class RequestSerImpl extends BaseServiceImpl<Request, RequestDao> implements RequestService {
    public RequestSerImpl(RequestDao repository, ClientService clientSer, JobService jobSer) {
        super(repository);
        this.clientSer = clientSer;
        this.jobSer = jobSer;
    }

    private final ClientService clientSer;
    private final JobService jobSer;


    @Override
    public Request saveNewRequest(Request request) {
        if (request.getId() != null)
            throw new InvalidIdException("new request's id must be null.");
        clientValidation(
                request.getClient().getId());
        jobValidation(
                request.getJob().getId());
        if (request.getProposedPrice() <= 0)
            throw new IllegalArgumentException("propose price must be positive.");
        if (request.getDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("past date cannot be chosen.");
        if (request.getDate().isEqual(LocalDate.now())) {
            if (request.getSuggestedTime().isBefore(LocalTime.now().plusMinutes(30)))
                throw new IllegalArgumentException("request's suggested time must be at least 30 minuets later from now.");
        }
        return saveOrUpdate(request);
    }

    private void jobValidation(Long jobId) {
        if (jobId == null)
            throw new NullIdException("job's id cannot be null.");
        if (jobSer.findById(jobId) == null)
            throw new IllegalArgumentException("job's id is not valid.");
    }

    private void clientValidation(Long clientId) {
        if (clientId == null)
            throw new NullIdException("client's id cannot be null.");
        if (clientSer.findById(clientId) == null)
            throw new IllegalArgumentException("client's id is not valid.");
    }
}