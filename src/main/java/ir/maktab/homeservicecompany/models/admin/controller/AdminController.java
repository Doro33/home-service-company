package ir.maktab.homeservicecompany.models.admin.controller;

import ir.maktab.homeservicecompany.models.admin.service.AdminService;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminSer;
    private final ClientService clientSer;

    private final WorkerService workerSer;

    public AdminController(AdminService adminSer, ClientService clientSer, WorkerService workerSer) {
        this.adminSer = adminSer;
        this.clientSer = clientSer;
        this.workerSer = workerSer;
    }

    @PostMapping("/addCategory")
    public void addCategory(@RequestBody Category category) {
        adminSer.addNewCategory(category.getName());
    }

    @PostMapping("/addJob")
    public void addJob(@RequestBody JobDTO jobDTO) {
        Category category = adminSer.findCategoryByName(jobDTO.getCategoryName()).get();
        Job job = new Job(category,
                jobDTO.getJobName(),
                jobDTO.getMinimumPrice(),
                jobDTO.getDescription());
        adminSer.addNewJob(job);
    }

    @PutMapping("/updateJob")
    public void updateJob(@RequestBody JobDTO jobDTO){
        adminSer.updateJob(jobDTO);
    }

    @PutMapping("/permitWorkerSkill/{id}")
    public void permitWorkerSkill(@PathVariable Long id){
        adminSer.permitWorkerSkill(id);
    }

    @PutMapping("/banWorkerSkill/{id}")
    public void banWorkerSkill(@PathVariable Long id){
        adminSer.banWorkerSkill(id);
    }

    @PutMapping("/confirmWorker/{id}")
    public void confirmWorker(@PathVariable Long id){
        adminSer.confirmWorker(id);
    }

    @GetMapping("/showAllClients")
    public List<Client> showAllClients(){
        return clientSer.findAll();
    }

    @GetMapping("/showAllWorkers")
    public List<Worker> showAllWorkers(){
        return workerSer.findAll();
    }
}
