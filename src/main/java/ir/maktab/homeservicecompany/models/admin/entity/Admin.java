package ir.maktab.homeservicecompany.models.admin.entity;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Admin extends BaseEntity {
    private Double credit;
}