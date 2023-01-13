package ir.maktab.homeservicecompany.models.job.service;

import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.category.service.CategoryService;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import ir.maktab.homeservicecompany.models.job.dao.JobDao;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSerImpl extends BaseServiceImpl<Job, JobDao> implements JobService{
    public JobSerImpl(JobDao repository, CategoryService categorySer){
        super(repository);
        this.categorySer = categorySer;
    }
    private final CategoryService categorySer;

    @Override
    public Job findByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    @Override
    public Job addNewJob(Job job) {
        if (job.getId()!=null)
            throw new InvalidIdException("new job's id must be null.");
        if (categorySer.findByName(job.getCategory().getName()).isEmpty())
            throw new IllegalArgumentException("this category does not exist.");
        if (findByName(job.getName())!=null)
            throw new IllegalArgumentException("this job has already been added");
        if (job.getMinimumPrice()<=0)
            throw new IllegalArgumentException("minimum price must be positive.");
        return saveOrUpdate(job);
    }

    @Override
    public Job updateJob(JobDTO jobDTO) {
        Job job=findByName(jobDTO.getJobName());
        if (job == null)
            throw new IllegalArgumentException("there is no job with this name");
        if (jobDTO.getMinimumPrice()<=0)
            throw new IllegalArgumentException("minimum price cannot be lesser than 0.");

        job.setMinimumPrice(jobDTO.getMinimumPrice());
        job.setDescription(jobDTO.getDescription());
        return saveOrUpdate(job);
    }

    @Override
    public List<Job> findByCategory(Category category) {
        return repository.findByCategory(category);
    }

}