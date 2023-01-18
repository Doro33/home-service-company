package ir.maktab.homeservicecompany.models.worker.service;

import com.google.common.base.Strings;
import ir.maktab.homeservicecompany.models.job.entity.Job;
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
import ir.maktab.homeservicecompany.utils.security.Role;
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
import java.util.Optional;


@Service
public class WorkerSerImpl extends BaseServiceImpl<Worker, WorkerDao> implements WorkerService {
    @PersistenceContext
    private EntityManager em;

    public WorkerSerImpl(WorkerDao repository, WorkerSkillService workerSkillSer, Validation validation) {
        super(repository);
        this.workerSkillSer = workerSkillSer;
        this.validation = validation;
    }


    private final WorkerSkillService workerSkillSer;
    private final Validation validation;

    @Override
    public Optional<Worker> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void changePassword(PasswordDTO passwordDTO) {
        Worker worker = findByEmail(passwordDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("this email does not have an account."));

        String newPassword = validation.checkPasswords(passwordDTO, worker.getPassword());

        worker.setPassword(newPassword);
        saveOrUpdate(worker);
    }


    @Override
    public Worker signUp(UserDTO userDTO, byte[] image) {
        String email = userDTO.getEmail();
        validation.emailValidation(email);

        Worker worker = new Worker(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPassword(),
                email,
                image);
        return saveOrUpdate(worker);
    }

    @Override
    public void confirmWorker(Long id) {
        Worker worker = validation.workerValidate(id);
        if (worker.getStatus() == WorkerStatus.SUSPENDED)
            throw new AdminPermitException("worker has been suspended.");
        if (worker.getStatus() == WorkerStatus.CONFIRMED)
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
        predicateMaker(filterWorkerDTO, predicateList, criteriaBuilder, root);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(root).where(predicates);
        return em.createQuery(query).getResultList();
    }

    @Override
    public void addSkill(Long workerId, Long jobId) {
        Worker worker = validation.workerValidate(workerId);
        Job job = validation.jobValidate(jobId);
        if (worker.getStatus() != WorkerStatus.CONFIRMED)
            throw new IllegalArgumentException("worker has not been confirmed.");

        if (workerSkillSer.existsByWorkerAndJob(worker, job))
            throw new IllegalArgumentException("this skill has already added for this worker.");

        WorkerSkill workerSkill = new WorkerSkill(worker, job);
        workerSkillSer.saveOrUpdate(workerSkill);
    }

    @Override
    public void activeWorker(Long id) {
        Worker worker = validation.workerValidate(id);
        if (worker.getRole() == Role.ROLE_WORKER)
            throw new IllegalArgumentException("this account has been activated already.");
        worker.setRole(Role.ROLE_WORKER);
        saveOrUpdate(worker);
    }


    private void predicateMaker(FilterWorkerDTO filterWorkerDTO, List<Predicate> predicateList,
                                CriteriaBuilder criteriaBuilder, Root<Worker> root) {
        String firstName = filterWorkerDTO.getFirstName();
        String lastName = filterWorkerDTO.getLastName();
        String email = filterWorkerDTO.getEmail();

        if (!Strings.isNullOrEmpty(firstName)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("firstName"), sampleMaker(firstName)));
        }
        if (!Strings.isNullOrEmpty(lastName)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("lastName"), sampleMaker(lastName)));
        }
        if (Strings.isNullOrEmpty(email)) {
            predicateList.add(criteriaBuilder
                    .like(root.get("email"), sampleMaker(email)));
        }
        scorePredicateMaker(filterWorkerDTO, predicateList, criteriaBuilder, root);
        completedTaskPredicateMaker(filterWorkerDTO,predicateList,criteriaBuilder,root);
    }

    private String sampleMaker(String input) {
        return "%" + input + "%";
    }

    private static void scorePredicateMaker(FilterWorkerDTO filterWorkerDTO, List<Predicate> predicateList,
                                            CriteriaBuilder criteriaBuilder, Root<Worker> root) {
        Long minScore = filterWorkerDTO.getMinScore();
        Long maxScore = filterWorkerDTO.getMaxScore();
        if (minScore == null)
            minScore = Long.MIN_VALUE;
        if (maxScore == null)
            maxScore = Long.MAX_VALUE;
        if (maxScore < minScore)
            throw new IllegalArgumentException("max score cannot be lesser than min score");
        predicateList.add(criteriaBuilder.
                between(root.get("score"), minScore, maxScore));
    }

    private static void completedTaskPredicateMaker(FilterWorkerDTO filterWorkerDTO, List<Predicate> predicateList,
                                                    CriteriaBuilder criteriaBuilder, Root<Worker> root) {
        Integer minCompletedTask = filterWorkerDTO.getMinCompletedTask();
        Integer maxCompletedTask = filterWorkerDTO.getMaxCompletedTask();
        if (minCompletedTask == null)
            minCompletedTask = 0;
         if (maxCompletedTask==null)
             maxCompletedTask=Integer.MAX_VALUE;
         if (maxCompletedTask<minCompletedTask)
             throw new IllegalArgumentException("max completed tasks cannot be lesser than min completed task");
        if (minCompletedTask < 0)
            throw new IllegalArgumentException("min completed tasks cannot be lesser than 0.");
        predicateList.add(criteriaBuilder.
                between(root.get("completedTaskCounter"), minCompletedTask, maxCompletedTask));
    }
}