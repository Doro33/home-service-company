package ir.maktab.homeservicecompany.models.job.service;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.job.entity.Job;

import java.util.List;

public interface JobService extends BaseService<Job> {
    Job findByName(String name);
     Job addNewJob(JobDTO jobDTO);

     Job updateJob(JobDTO jobDTO);

    List<Job> findByCategory(Category category);
}