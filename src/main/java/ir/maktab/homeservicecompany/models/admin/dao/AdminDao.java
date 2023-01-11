package ir.maktab.homeservicecompany.models.admin.dao;

import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin,Long>{
}
