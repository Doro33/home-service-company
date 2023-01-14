package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.worker.entity.WorkerStatus;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import ir.maktab.homeservicecompany.models.worker_skill.service.WorkerSkillService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.worker.dao.WorkerDao;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import ir.maktab.homeservicecompany.utils.exception.AdminPermitException;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkerSerImpl extends BaseServiceImpl<Worker, WorkerDao> implements WorkerService {
    @PersistenceContext
    private EntityManager em;

    public WorkerSerImpl(WorkerDao repository, @Lazy OfferService offerSer, JobService jobSer, WorkerSkillService workerSkillSer, Validation validation) {
        super(repository);
        this.offerSer = offerSer;
        this.jobSer = jobSer;
        this.workerSkillSer = workerSkillSer;
        this.validation = validation;
    }
    private final OfferService offerSer;
    private final JobService jobSer;
    private final WorkerSkillService workerSkillSer;
    private final Validation validation;

    @Override
    public Worker findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public Worker changePassword(PasswordDTO passwordDTO) {
        String email= passwordDTO.getEmail();
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword1 = passwordDTO.getNewPassword1();
        String newPassword2 = passwordDTO.getNewPassword2();
        Worker worker = findByEmail(email);

        if (worker == null)
            throw new IllegalArgumentException("this email does not have an account.");
        if (!oldPassword.equals(worker.getPassword()))
            throw new IllegalArgumentException("incorrect password");
        if (!newPassword2.matches(newPassword1))
            throw new IllegalArgumentException("new passwords are not match.");
        validation.passwordValidate(newPassword1);

        worker.setPassword(newPassword1);
        return saveOrUpdate(worker);
    }

    @Override
    public Worker signUp(Worker worker) {
        if (worker.getId() != null) throw new InvalidIdException("new worker's id must be null.");
        validation.nameValidate(worker.getFirstName(), worker.getLastName());
        if (findByEmail(worker.getEmail()) != null) throw new IllegalArgumentException("this email has been used.");
        return saveOrUpdate(worker);
    }

    @Override
    public Worker confirmWorker(Long id) {
        if (id==null)
            throw new NullIdException("worker id cannot be null.");
        Worker worker = findById(id);
        if (worker.getStatus()== WorkerStatus.SUSPENDED)
            throw new AdminPermitException("worker has been suspended.");
        if (worker.getStatus()== WorkerStatus.CONFIRMED)
            throw new AdminPermitException("worker has been already confirmed.");
        worker.setStatus(WorkerStatus.CONFIRMED);
        return saveOrUpdate(worker);
    }
    @Override
    public List<Worker> workerCriteria(WorkerDto workerDto) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Worker> query = criteriaBuilder.createQuery(Worker.class);
        Root<Worker> root = query.from(Worker.class);
        createPredicates(workerDto, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    @Override
    public WorkerSkill addSkill(Long workerId,Long jobId) {
        if (workerId==null)
            throw new NullIdException("worker id cannot be null.");
        if (jobId==null)
            throw new NullIdException("job id cannot be null.");

        Worker worker = findById(workerId);
        Job job = jobSer.findById(jobId);
        if (worker.getStatus()!=WorkerStatus.CONFIRMED)
            throw new IllegalArgumentException("worker has not been confirmed.");

        if (workerSkillSer.existsByWorkerAndJob(worker,job))
            throw new IllegalArgumentException("this skill has already added for this worker.");

        WorkerSkill workerSkill= new WorkerSkill(worker,job);
        System.out.println(workerSkill);
        return workerSkillSer.saveOrUpdate(workerSkill);
    }

    private void createPredicates(WorkerDto workerDto, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Worker> root) {
        Long minScore = workerDto.getMinScore();
        Long maxScore = workerDto.getMaxScore();
        if (minScore == null && maxScore != null) {
            predicateList.add(criteriaBuilder.between(root.get("score"), 0L, maxScore));
        } else if (minScore != null && maxScore == null) {
            predicateList.add(criteriaBuilder.between(root.get("score"), minScore, Long.MAX_VALUE));
        } else if (minScore != null) {
            predicateList.add(criteriaBuilder.between(root.get("score"), minScore, maxScore));
        }
    }
}