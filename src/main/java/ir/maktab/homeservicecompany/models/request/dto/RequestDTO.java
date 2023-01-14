package ir.maktab.homeservicecompany.models.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class RequestDTO {
    private Long clientId;
    private Long jobId;
    private Double proposedPrice;
    private String description;
    private LocalDate date;
    private LocalTime suggestedTime;
    private String address;
}
