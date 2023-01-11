package ir.maktab.homeservicecompany.models.offer.entity;

import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@NotNull
public class Offer extends BaseEntity {
    public Offer(Worker worker, Request request, Double expectedPrice, Duration expectedDuration) {
        this.worker = worker;
        this.request = request;
        this.createdAt = LocalDateTime.now();
        this.expectedPrice = expectedPrice;
        this.expectedDuration = expectedDuration;
        this.clientAccepted = null;
    }

    @ManyToOne
    private Worker worker;
    @ManyToOne
    private Request request;
    private LocalDateTime createdAt;
    @Positive
    private Double expectedPrice;
    private Duration expectedDuration;
    @Nullable
    private Boolean clientAccepted;
}