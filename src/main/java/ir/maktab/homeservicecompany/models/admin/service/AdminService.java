package ir.maktab.homeservicecompany.models.admin.service;

import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;

import java.util.List;
import java.util.Optional;

public interface AdminService extends BaseService<Admin> {
    Category addNewCategory(String name);

    Optional<Category> findCategoryByName(String name);
    List<Category> findAllCategories();
    Job addNewJob(Job job);

    Job updateJob(JobDTO jobDTO);


    List<Job> findAllJobs();
    void permitWorkerSkill(Long id);

    void banWorkerSkill(Long id);

    void confirmWorker(Long id);
    List<Client> clientCriteria(ClientDTO clientDto);

    List<Worker> workerCriteria(WorkerDto workerDto);
}
