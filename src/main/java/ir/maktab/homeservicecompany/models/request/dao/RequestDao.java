package ir.maktab.homeservicecompany.models.request.dao;

import ir.maktab.homeservicecompany.models.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDao extends JpaRepository<Request,Long> {
}