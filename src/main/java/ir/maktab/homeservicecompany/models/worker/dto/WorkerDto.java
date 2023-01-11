package ir.maktab.homeservicecompany.models.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Long minScore;
    private Long maxScore;
}
