package ir.maktab.homeservicecompany.models.worker_skill.service;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;

import java.util.List;

public interface WorkerSkillService extends BaseService<WorkerSkill> {

    void permitWorkerSkill(Long id);

    void banWorkerSkill(Long id);

    List<WorkerSkill> findByWorker(Worker worker);

    List<Job> findWorkerSkills(Worker worker);

    boolean existsByWorkerAndJob(Worker worker, Job job);
}