package ir.maktab.homeservicecompany.models.admin.service;

import ir.maktab.homeservicecompany.models.admin.dao.AdminDao;
import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.category.service.CategoryService;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker_skill.service.WorkerSkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminSerImpl extends BaseServiceImpl<Admin, AdminDao> implements AdminService{

    private final CategoryService categorySer;
    private final JobService jobSer;
    private final WorkerSkillService workerSkillSer;

    private final ClientService clientSer;
    private final WorkerService workerSer;

    public AdminSerImpl(AdminDao repository, CategoryService categorySer, JobService jobSer, WorkerSkillService workerSkillSer, ClientService clientSer, WorkerService workerSer) {
        super(repository);
        this.categorySer = categorySer;
        this.jobSer = jobSer;
        this.workerSkillSer = workerSkillSer;
        this.clientSer = clientSer;
        this.workerSer = workerSer;
    }

    @Override
    public Category addNewCategory(String name) {
        return categorySer.addNewCategory(name);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categorySer.findByName(name);
    }

    @Override
    public Job addNewJob(Job job) {
        return jobSer.addNewJob(job);
    }

    @Override
    public Job updateJob(JobDTO jobDTO) {
        return jobSer.updateJob(jobDTO);
    }

    @Override
    public void permitWorkerSkill(Long id) {
        workerSkillSer.permitWorkerSkill(id);
    }

    @Override
    public void banWorkerSkill(Long id) {
        workerSkillSer.banWorkerSkill(id);
    }

    @Override
    public void confirmWorker(Long id) {
        workerSer.confirmWorker(id);
    }

    @Override
    public List<Client> clientCriteria(ClientDTO clientDto) {
        return clientSer.clientCriteria(clientDto);
    }

    @Override
    public List<Worker> workerCriteria(WorkerDto workerDto) {
        return workerSer.workerCriteria(workerDto);
    }
}