package ir.maktab.homeservicecompany.models.request.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@NotNull
public class Request extends BaseEntity {
    public Request(Client client, Job job, Double proposedPrice, String description,
                   LocalDate date, LocalTime suggestedTime, String address) {
        this.client = client;
        this.job = job;
        this.proposedPrice = proposedPrice;
        this.description = description;
        this.date = date;
        this.suggestedTime = suggestedTime;
        this.address = address;
        this.status = RequestStatus.AWAITING_FOR_OFFERS;
        this.acceptedOffer = null;
    }

    @ManyToOne
    private Client client;
    @ManyToOne
    private Job job;
    @Positive
    private Double proposedPrice;
    private String description;
    private LocalDate date;
    private LocalTime suggestedTime;
    private String address;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @ManyToOne
    @Nullable
    private Offer acceptedOffer;
    @Nullable
    private LocalTime startAt;
    @Nullable
    private LocalTime endAt;
}