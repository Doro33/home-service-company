package ir.maktab.homeservicecompany.models.worker_skill.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class WorkerSkillId implements Serializable {
    private Long workerId;
    private Long jobId;
}
