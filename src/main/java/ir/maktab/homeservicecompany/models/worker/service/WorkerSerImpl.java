package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.worker.dto.FilterWorkerDTO;
import ir.maktab.homeservicecompany.utils.dto.UserDTO;
import ir.maktab.homeservicecompany.models.worker.entity.WorkerStatus;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import ir.maktab.homeservicecompany.models.worker_skill.service.WorkerSkillService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.worker.dao.WorkerDao;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import ir.maktab.homeservicecompany.utils.exception.AdminPermitException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkerSerImpl extends BaseServiceImpl<Worker, WorkerDao> implements WorkerService {
    @PersistenceContext
    private EntityManager em;

    public WorkerSerImpl(WorkerDao repository, JobService jobSer, WorkerSkillService workerSkillSer, Validation validation) {
        super(repository);
        this.jobSer = jobSer;
        this.workerSkillSer = workerSkillSer;
        this.validation = validation;
    }

    private final JobService jobSer;
    private final WorkerSkillService workerSkillSer;
    private final Validation validation;

    @Override
    public Worker findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public void changePassword(PasswordDTO passwordDTO) {
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
        saveOrUpdate(worker);
    }

    @Override
    public void signUp(UserDTO userDTO, byte[] image) {
        String firstName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        validation.nameValidate(firstName, lastName);
        if (findByEmail(email) != null)
            throw new IllegalArgumentException("this email has been used.");
        Worker worker = new Worker(firstName,lastName,email,password , image);
        saveOrUpdate(worker);
    }

    @Override
    public void confirmWorker(Long id) {
        if (id==null)
            throw new NullIdException("worker id cannot be null.");
        Worker worker = findById(id);
        if (worker.getStatus()== WorkerStatus.SUSPENDED)
            throw new AdminPermitException("worker has been suspended.");
        if (worker.getStatus()== WorkerStatus.CONFIRMED)
            throw new AdminPermitException("worker has been already confirmed.");
        worker.setStatus(WorkerStatus.CONFIRMED);
        saveOrUpdate(worker);
    }
    @Override
    public List<Worker> workerCriteria(FilterWorkerDTO filterWorkerDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Worker> query = criteriaBuilder.createQuery(Worker.class);
        Root<Worker> root = query.from(Worker.class);
        createPredicates(filterWorkerDTO, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    @Override
    public void addSkill(Long workerId,Long jobId) {
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
        workerSkillSer.saveOrUpdate(workerSkill);
    }

    private void createPredicates(FilterWorkerDTO filterWorkerDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Worker> root) {

    }
}