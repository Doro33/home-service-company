package ir.maktab.homeservicecompany.models.comment.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.comment.dto.CommentDTO;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.comment.dao.CommentDao;
import ir.maktab.homeservicecompany.models.comment.entity.Comment;
import ir.maktab.homeservicecompany.utils.exception.RequestStatusException;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentSerImpl extends BaseServiceImpl<Comment, CommentDao> implements CommentService {
    private final WorkerService workerSer;
    private final Validation validation;

    public CommentSerImpl(CommentDao repository, WorkerService workerSer, Validation validation) {
        super(repository);
        this.workerSer = workerSer;
        this.validation = validation;
    }

    @Override
    @Transactional
    public void addComment(CommentDTO commentDTO) {
        Client client = validation.clientValidate(commentDTO.getClientId());
        Request request = validation.requestValidate(commentDTO.getRequestId());
        RequestStatus status = request.getStatus();
        Worker worker = request.getAcceptedOffer().getWorker();
        Boolean commentPermit= existsByRequest(request);
        if (request.getClient() != client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
        if (Boolean.TRUE.equals(commentPermit))
            throw new IllegalArgumentException("this request has been already commented.");
        if (!(status == RequestStatus.COMPLETED || status == RequestStatus.PAID))
            throw new RequestStatusException("this request must be completed first.");

       worker.commentEffect(commentDTO.getRating());
        workerSer.saveOrUpdate(worker);

        Comment comment = new Comment(request, commentDTO.getRating(), commentDTO.getDescription());
        saveOrUpdate(comment);
    }

    @Override
    public Boolean existsByRequest(Request request) {
        return repository.existsByRequest(request);
    }

}