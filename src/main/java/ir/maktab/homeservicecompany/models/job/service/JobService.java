package ir.maktab.homeservicecompany.models.job.service;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.job.entity.Job;

import java.util.List;
import java.util.Optional;

public interface JobService extends BaseService<Job> {
    Optional<Job> findByName(String name);
     void addNewJob(JobDTO jobDTO);

     void updateJob(JobDTO jobDTO);

    List<Job> findByCategory(Category category);
}