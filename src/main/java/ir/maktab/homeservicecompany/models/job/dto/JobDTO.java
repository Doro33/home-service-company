package ir.maktab.homeservicecompany.models.job.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobDTO {
    private String categoryName;
    private String jobName;
    private Double minimumPrice;
    @Nullable
    private String description;
}
