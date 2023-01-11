package ir.maktab.homeservicecompany.models.worker_skill.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker_skill.dao.WorkerSkillDao;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.utils.exception.NullIdException;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkerSkillSerImpl implements WorkerSkillService{
    private final WorkerSkillDao repository;

    public WorkerSkillSerImpl(WorkerSkillDao repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public WorkerSkill saveOrUpdate(WorkerSkill workerSkill) {
        return repository.save(workerSkill);
    }

    @Override
    public void delete(WorkerSkill workerSkill) {
        repository.delete(workerSkill);
    }

    @Override
    public WorkerSkill findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new InvalidIdException("This id is not valid."));
    }

    @Override
    public List<WorkerSkill> findAll() {
        return repository.findAll();
    }

    @Override
    public void permitWorkerSkill(WorkerSkill workerSkill) {
        if (workerSkill.getId()==null)
            throw new NullIdException("workerSkill id cannot be null.");
        else{
            workerSkill.setConfirmedByAdmin(true);
            saveOrUpdate(workerSkill);
        }
    }

    @Override
    public void banWorkerSkill(WorkerSkill workerSkill) {
        if (workerSkill.getId()==null)
            throw new NullIdException("workerSkill id cannot be null.");
        else{
            workerSkill.setConfirmedByAdmin(false);
            saveOrUpdate(workerSkill);
        }
    }

    @Override
    public List<WorkerSkill> findByWorker(Worker worker) {
        return repository.findByWorker(worker);
    }

    @Override
    public List<Job> findWorkerSkills(Worker worker) {
        return findByWorker(worker).stream().map(WorkerSkill::getJob).toList();
    }
}