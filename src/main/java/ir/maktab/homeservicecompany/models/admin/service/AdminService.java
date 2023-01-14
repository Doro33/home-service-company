package ir.maktab.homeservicecompany.models.admin.service;

import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;

import java.util.Optional;

public interface AdminService extends BaseService<Admin> {
    Category addNewCategory(String name);

    Optional<Category> findCategoryByName(String name);

    Job updateJob(JobDTO jobDTO);
    void permitWorkerSkill(Long id);

    void banWorkerSkill(Long id);

    void confirmWorker(Long id);
}
