package ir.maktab.homeservicecompany.models.request.dto;

import ir.maktab.homeservicecompany.models.request.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class FilterRequestDTO {
    private Long categoryId;
    private Long jobId;
    private RequestStatus requestStatus;
    private LocalDate requestedAfter;
    private LocalDate requestedBefore;
}
