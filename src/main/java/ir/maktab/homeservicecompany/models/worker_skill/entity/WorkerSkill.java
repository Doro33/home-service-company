package ir.maktab.homeservicecompany.models.worker_skill.entity;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@NotNull
public class WorkerSkill extends BaseEntity {

    public WorkerSkill(Worker worker, Job job) {
        this.worker = worker;
        this.job = job;
        this.requestedAt = LocalDateTime.now();
        this.confirmedByAdmin = null;
    }

   /* @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private WorkerSkillId id;*/
    @ManyToOne()
    @MapsId("workerId")
    private Worker worker;
    @ManyToOne
    @MapsId("jobId")
    private Job job;
    private LocalDateTime requestedAt;
    @Nullable
    private Boolean confirmedByAdmin;
}