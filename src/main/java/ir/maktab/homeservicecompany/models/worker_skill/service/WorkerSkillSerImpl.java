package ir.maktab.homeservicecompany.models.worker_skill.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.WorkerStatus;
import ir.maktab.homeservicecompany.models.worker_skill.dao.WorkerSkillDao;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.utils.exception.AdminPermitException;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkerSkillSerImpl extends BaseServiceImpl<WorkerSkill,WorkerSkillDao> implements WorkerSkillService{

    public WorkerSkillSerImpl(WorkerSkillDao repository) {
        super(repository);
    }

    @Override
    public void permitWorkerSkill(Long id) {
        if (id==null)
            throw new NullIdException("workerSkill id cannot be null.");
        WorkerSkill workerSkill = findById(id);
        if (workerSkill.getWorker().getStatus()!= WorkerStatus.CONFIRMED)
            throw new AdminPermitException("worker is not confirmed.");
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
    public List<WorkerSkill> findByWorker(Worker worker) {
        return repository.findByWorker(worker);
    }

    @Override
    public List<Job> findWorkerSkills(Worker worker) {
        return findByWorker(worker).stream().map(WorkerSkill::getJob).toList();
    }

    @Override
    public boolean existsByWorkerAndJob(Worker worker, Job job) {
        return repository.existsByWorkerAndJob(worker,job);
    }

    @Override
    public boolean canWorkerDoThisJob(Worker worker, Job job) {
        return repository.existsByWorkerAndJobAndConfirmedByAdminIsTrue(worker, job);
    }
}