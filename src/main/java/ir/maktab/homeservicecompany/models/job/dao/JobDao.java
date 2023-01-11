package ir.maktab.homeservicecompany.models.job.dao;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobDao extends JpaRepository<Job,Long> {
    Optional<Job> findByName(String name);

    List<Job> findByCategory(Category category);
}