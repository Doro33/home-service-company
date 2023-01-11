package ir.maktab.homeservicecompany.models.category.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@NotNull
public class Category extends BaseEntity {
    @Column(unique = true)
    private String name;
}