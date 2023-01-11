package ir.maktab.homeservicecompany.models.worker_skill.entity;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@NotNull
public class WorkerSkill extends BaseEntity {

    public WorkerSkill(Worker worker, Job job) {
        this.worker = worker;
        this.job = job;
        this.requestedAt = LocalDateTime.now();
        this.confirmedByAdmin = null;
    }

    @ManyToOne()
    private Worker worker;
    @ManyToOne
    private Job job;
    private LocalDateTime requestedAt;
    @Nullable
    private Boolean confirmedByAdmin;
}