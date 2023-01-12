package ir.maktab.homeservicecompany.utils.controller;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.category.service.CategoryService;
import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general")
public class GeneralController {
    private final CategoryService categorySer;
    private final JobService jobSer;
    private final ClientService clientSer;

    private final RequestService requestSer;

    public GeneralController(CategoryService categorySer, JobService jobSer, ClientService clientSer, RequestService requestSer) {
        this.categorySer = categorySer;
        this.jobSer = jobSer;
        this.clientSer = clientSer;
        this.requestSer = requestSer;
    }

    @GetMapping("/findAllCategories")
    public List<Category> findAllCategories() {
        return categorySer.findAll();
    }

    @GetMapping("/findAllJobs")
    public List<Job> findAllJob() {
        return jobSer.findAll();
    }

    @PostMapping("/clientSignup")
    public void clientSignUp(@RequestBody ClientDTO clientDTO) {
        clientSer.signUp(
                new Client(
                        clientDTO.getFirstName(),
                        clientDTO.getLastName(),
                        clientDTO.getEmail(),
                        clientDTO.getPassword()));
    }

    @GetMapping("findRequestsByJob/{id}")
    public List<Request> findRequestsByJobId(@PathVariable Long id){
        return requestSer.findByJob(id);
    }
}
