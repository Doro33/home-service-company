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
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.utils.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserService userSer;

    private PasswordEncoder passEncoder;

    public Validation(@Lazy ClientService clientSer, @Lazy RequestService requestSer, @Lazy WorkerService workerSer,
                      @Lazy OfferService offerSer, @Lazy JobService jobSer, UserService userSer) {
        this.clientSer = clientSer;
        this.requestSer = requestSer;
        this.workerSer = workerSer;
        this.offerSer = offerSer;
        this.jobSer = jobSer;
        this.userSer = userSer;
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
        return clientSer.findById(clientId);
    }

    public Request requestValidate(Long requestId) {
        if (requestId == null)
            throw new NullIdException("request's id cannot be null.");
        return requestSer.findById(requestId);
    }

    public Worker workerValidate(Long workerId) {
        if (workerId == null)
            throw new NullIdException("worker's id cannot be null.");
        return workerSer.findById(workerId);
    }

    public Offer offerValidate(Long offerId) {
        if (offerId == null)
            throw new NullIdException("offer's id cannot be null.");
        return offerSer.findById(offerId);
    }

    public Job jobValidate(Long jobId) {
        if (jobId == null)
            throw new NullIdException("job's id cannot be null.");
        return jobSer.findById(jobId);
    }

    public String checkPasswords(PasswordDTO passwordDTO, String truePassword) {
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword1 = passwordDTO.getNewPassword1();
        String newPassword2 = passwordDTO.getNewPassword2();
        if (!newPassword2.matches(newPassword1))
            throw new IllegalArgumentException("new passwords are not match.");
        if (!passEncoder.matches(oldPassword,truePassword))
            throw new IllegalArgumentException("incorrect password");
        return newPassword1;
    }

    public void emailValidation (String email){
        if (Strings.isNullOrEmpty(email))
            throw new IllegalArgumentException("email cannot be empty.");
        if (!userSer.signUpPermit(email))
            throw new IllegalArgumentException("this email has been used already");
    }
}
