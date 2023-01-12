package ir.maktab.homeservicecompany.models.offer.dto;

import lombok.Getter;

@Getter
public class OfferDTO {
    private Long workerId;
    private Long requestId;
    private Double expectedPrice;
    private Integer expectedDurationByHour;
}
