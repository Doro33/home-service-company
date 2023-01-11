package ir.maktab.homeservicecompany.models.worker_skill.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;

import java.util.List;

public interface WorkerSkillService  {
    WorkerSkill saveOrUpdate(WorkerSkill workerSkill);

    void delete(WorkerSkill workerSkill);

    WorkerSkill findById(Long id);

    List<WorkerSkill> findAll();

    void permitWorkerSkill(WorkerSkill workerSkill);

    void banWorkerSkill(WorkerSkill workerSkill);

    List<WorkerSkill> findByWorker(Worker worker);

    List<Job> findWorkerSkills(Worker worker);
}