package ir.maktab.homeservicecompany.models.request.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
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
    public Request saveNewRequest(RequestDTO requestDTO) {
        Client client = clientValidation(requestDTO.getClientId());
        Job job = jobValidation(requestDTO.getJobId());
        Request request = requestMaker(requestDTO, client, job);



        client.setRequestCounter(
                client.getRequestCounter()+1
        );
        clientSer.saveOrUpdate(client);
        return saveOrUpdate(request);
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

    private Job jobValidation(Long jobId) {
        if (jobId == null) throw new NullIdException("job's id cannot be null.");
        Job job = jobSer.findById(jobId);
        if (job == null) throw new IllegalArgumentException("job's id is not valid.");
        return job;
    }

    private Client clientValidation(Long clientId) {
        if (clientId == null) throw new NullIdException("client's id cannot be null.");
        Client client = clientSer.findById(clientId);
        if (client == null) throw new IllegalArgumentException("client's id is not valid.");

        System.out.println(client);
        return client;
    }
}