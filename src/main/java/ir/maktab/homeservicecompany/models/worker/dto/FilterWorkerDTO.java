package ir.maktab.homeservicecompany.models.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterWorkerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long minScore;
    private Long maxScore;
    private Integer minCompletedTask;
    private Integer maxCompletedTask;
}
