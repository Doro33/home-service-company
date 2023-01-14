package ir.maktab.homeservicecompany.models.worker_skill.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.WorkerStatus;
import ir.maktab.homeservicecompany.models.worker_skill.dao.WorkerSkillDao;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.utils.exception.AdminPermitException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerSkillSerImpl extends BaseServiceImpl<WorkerSkill,WorkerSkillDao> implements WorkerSkillService{
    private final Validation validation;

    public WorkerSkillSerImpl(WorkerSkillDao repository, Validation validation) {
        super(repository);
        this.validation = validation;
    }

    @Override
    public void permitWorkerSkill(Long id) {
        if (id==null)
            throw new NullIdException("workerSkill id cannot be null.");
        WorkerSkill workerSkill = findById(id);
        if (workerSkill.getWorker().getStatus()!= WorkerStatus.CONFIRMED)
            throw new AdminPermitException("worker must be confirmed first.");
        workerSkill.setConfirmedByAdmin(true);
        saveOrUpdate(workerSkill);
    }

    @Override
    public void banWorkerSkill(Long id) {
        if (id==null)
            throw new NullIdException("workerSkill id cannot be null.");
        WorkerSkill workerSkill = findById(id);
        workerSkill.setConfirmedByAdmin(false);
        saveOrUpdate(workerSkill);
    }

    @Override
    public boolean existsByWorkerAndJob(Worker worker, Job job) {
        return repository.existsByWorkerAndJob(worker,job);
    }

    @Override
    public boolean canWorkerDoThisJob(Worker worker, Job job) {
        return repository.existsByWorkerAndJobAndConfirmedByAdminIsTrue(worker, job);
    }

    @Override
    public List<Worker> findWorkerByJobId(Long jobId) {
        validation.jobValidate(jobId);
        return repository.findWorkerByJobId(jobId);
    }
}