package ir.maktab.homeservicecompany.models.request.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.request.dao.RequestDao;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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


    @Override
    public Request saveNewRequest(RequestDTO requestDTO) {
        Client client = validation.clientValidate(requestDTO.getClientId());
        Job job = validation.jobValidate(requestDTO.getJobId());
        Request request = requestMaker(requestDTO, client, job);

        client.setRequestCounter(
                client.getRequestCounter()+1
        );
        clientSer.saveOrUpdate(client);
        return saveOrUpdate(request);
    }

    @Override
    public List<Request> findByJob(Long jobId) {
        Job job = jobSer.findById(jobId);
        return repository.findByJobAndAcceptedOfferIsNullOrderByDate(job);
    }

    @Override
    public List<Request> findByClient(Long clientId) {
        Client client= validation.clientValidate(clientId);
        return repository.findByClient(client);
    }

    private static Request requestMaker(RequestDTO requestDTO, Client client, Job job) {
        if (requestDTO.getProposedPrice() <= job.getMinimumPrice())
            throw new IllegalArgumentException("propose price must be at least." + job.getMinimumPrice());
        if (requestDTO.getDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("past date cannot be chosen.");
        if (requestDTO.getDate().isEqual(LocalDate.now())) {
            if (requestDTO.getSuggestedTime().isBefore(LocalTime.now().plusMinutes(30)))
                throw new IllegalArgumentException("request's suggested time must be at least 30 minuets later from now.");
        }
        return new Request(client, job,
                requestDTO.getProposedPrice(),
                requestDTO.getDescription(),
                requestDTO.getDate(),
                requestDTO.getSuggestedTime(),
                requestDTO.getAddress());
    }
}