package ir.maktab.homeservicecompany.models.worker.dto;

import lombok.Getter;

@Getter
public class FilterWorkerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long minScore;
    private Long maxScore;
    private Integer minCompletedTask;
    private Integer maxCompletedTask;
}
