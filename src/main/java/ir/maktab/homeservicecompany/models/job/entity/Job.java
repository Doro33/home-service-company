package ir.maktab.homeservicecompany.models.job.entity;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@NotNull
public class Job extends BaseEntity {
    @ManyToOne
    private Category category;
    @Column(unique = true)
    private String name;
    private Double minimumPrice;
    @Nullable
    private String description;
}