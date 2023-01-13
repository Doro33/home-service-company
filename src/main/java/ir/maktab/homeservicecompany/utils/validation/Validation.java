package ir.maktab.homeservicecompany.utils.validation;

import com.google.common.base.Strings;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class Validation {
    private final ClientService clientSer;
    private final RequestService requestSer;
    private final WorkerService workerSer;
    private final OfferService offerSer;

    private final JobService jobSer;

    public Validation(@Lazy ClientService clientSer,@Lazy RequestService requestSer, @Lazy WorkerService workerSer,
                      @Lazy OfferService offerSer,@Lazy JobService jobSer) {
        this.clientSer = clientSer;
        this.requestSer = requestSer;
        this.workerSer = workerSer;
        this.offerSer = offerSer;
        this.jobSer = jobSer;
    }

    public void nameValidate(String firstName, String lastName) {
        if (Strings.isNullOrEmpty(firstName))
            throw new IllegalArgumentException("first name cannot be null or empty.");
        if (Strings.isNullOrEmpty(lastName))
            throw new IllegalArgumentException("last name cannot be null or empty.");
    }

    public void passwordValidate(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8}$"))
            throw new IllegalArgumentException("""
                     password must contain at least 1 uppercase or lowercase and 1 digit.
                     password must have exactly 8 characters.
                    """);
    }

    public void imageValidate(MultipartFile image) {
        String[] choppedName = image.getName().split("\\.");
        int lastIndex = choppedName.length - 1;
        if (!Objects.equals(choppedName[lastIndex].toLowerCase(), "jpg")) {
            throw new IllegalArgumentException("image's format must be jpg.");
        }
        if (image.getSize() > 300_000L) {
            throw new IllegalArgumentException("image's size cannot be greater than 300kb.");
        }
    }

    public Client clientValidate(Long clientId) {
        if (clientId == null) throw new NullIdException("client's id cannot be null.");
        Client client = clientSer.findById(clientId);
        if (client == null) throw new IllegalArgumentException("client's id is not valid.");
        return client;
    }

    public Request requestValidate(Long requestId) {
        if (requestId == null)
            throw new NullIdException("request's id cannot be null.");
        Request request = requestSer.findById(requestId);
        if (request == null)
            throw new IllegalArgumentException("request's id is not valid.");
        return request;
    }

    public Worker workerValidate(Long workerId) {
        if (workerId == null)
            throw new NullIdException("worker's id cannot be null.");
        Worker worker = workerSer.findById(workerId);
        if (worker == null)
            throw new IllegalArgumentException("worker's id is not valid.");
        return worker;
    }

    public Offer offerValidate(Long offerId) {
        if (offerId == null)
            throw new NullIdException("offer's id cannot be null.");
        Offer offer = offerSer.findById(offerId);
        if (offer == null)
            throw new IllegalArgumentException("offer's id is not valid.");
        return offer;
    }

    public Job jobValidate(Long jobId) {
        if (jobId == null)
            throw new NullIdException("job's id cannot be null.");
        Job job = jobSer.findById(jobId);
        if (job == null)
            throw new IllegalArgumentException("job's id is not valid.");
        return job;
    }
}
