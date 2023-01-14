package ir.maktab.homeservicecompany.models.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferDTO {
    private Long workerId;
    private Long requestId;
    private Double expectedPrice;
    private Integer expectedDurationByHour;
}
