package ir.maktab.homeservicecompany.models.job.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class JobDTO {
    private String categoryName;
    private String jobName;
    private Double minimumPrice;
    @Nullable
    private String description;
}
