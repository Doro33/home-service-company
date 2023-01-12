package ir.maktab.homeservicecompany.models.request.dao;

import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDao extends JpaRepository<Request,Long> {
    List<Request> findByJobAndAcceptedOfferIsNullOrderByDate(Job job);
}