package ir.maktab.homeservicecompany.models.worker_skill.dao;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerSkillDao extends JpaRepository<WorkerSkill,Long> {
    @Query("""
              select w.worker from WorkerSkill as w
              where w.job.id=:jobId
""")
    List<Worker> findWorkerByJobId(Long jobId);
    List<WorkerSkill> findByWorker(Worker worker);
    boolean existsByWorkerAndJob(Worker worker, Job job);

    boolean existsByWorkerAndJobAndConfirmedByAdminIsTrue(Worker worker, Job job);
}