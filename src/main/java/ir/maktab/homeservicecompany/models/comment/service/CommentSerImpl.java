package ir.maktab.homeservicecompany.models.comment.service;

import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.comment.dao.CommentDao;
import ir.maktab.homeservicecompany.models.comment.entity.Comment;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.utils.exception.RequestStatusException;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentSerImpl extends BaseServiceImpl<Comment, CommentDao> implements CommentService {
    private final ClientService clientSer;
    private final WorkerService workerSer;

    public CommentSerImpl(CommentDao repository, ClientService clientSer, WorkerService workerSer) {
        super(repository);
        this.clientSer = clientSer;
        this.workerSer = workerSer;
    }

    @Override
    @Transactional
    public Comment addComment(Client client, Comment comment) {
        Request request = comment.getRequest();
        RequestStatus status = request.getStatus();
        Worker worker = request.getAcceptedOffer().getWorker();

        clientValidation(client.getId());

        if (request.getClient() != client)
            throw new IllegalArgumentException("request doesn't belong to this client.");
        if (!(status == RequestStatus.COMPLETED || status == RequestStatus.PAID))
            throw new RequestStatusException("this request must be completed first.");

       worker.commentEffect(comment.getRating());

        workerSer.saveOrUpdate(worker);
        return saveOrUpdate(comment);
    }

    private void clientValidation(Long clientId) {
        if (clientId == null)
            throw new NullIdException("client's id cannot be null.");
        if (clientSer.findById(clientId) == null)
            throw new IllegalArgumentException("client's id is not valid.");
    }
}