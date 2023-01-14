package ir.maktab.homeservicecompany.models.admin.controller;

import ir.maktab.homeservicecompany.models.admin.service.AdminService;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.client.dto.FilterClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.dto.JobDTO;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.dto.FilterWorkerDTO;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.models.worker_skill.service.WorkerSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminSer;
    private final ClientService clientSer;
    private final RequestService requestSer;
    private final WorkerService workerSer;
    private final OfferService offerSer;

    private final JobService jobSer;

    private final WorkerSkillService workerSkillSer;


    public AdminController(AdminService adminSer, ClientService clientSer, RequestService requestSer,
                           WorkerService workerSer, OfferService offerSer, JobService jobSer,
                           WorkerSkillService workerSkillSer) {
        this.adminSer = adminSer;
        this.clientSer = clientSer;
        this.requestSer = requestSer;
        this.workerSer = workerSer;
        this.offerSer = offerSer;
        this.jobSer = jobSer;
        this.workerSkillSer = workerSkillSer;
    }

    @PostMapping("/addCategory")
    public void addCategory(@RequestBody Category category) {
        adminSer.addNewCategory(category.getName());
    }

    @PostMapping("/addJob")
    public void addJob(@RequestBody JobDTO jobDTO) {
        jobSer.addNewJob(jobDTO);
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

    @GetMapping("/showClientRequest/{clientId}")
    public List<Request> showClientRequests(@PathVariable Long clientId){
        return requestSer.findByClient(clientId);
    }

    @GetMapping("/showWorkerOffers/{workerId}")
    public List<Offer> showWorkerOffers(@PathVariable Long workerId){
        return offerSer.findByWorker(workerId);
    }

    @GetMapping("/showJobWorkers/{jobId}")
    public List<Worker> showJobWorkers(@PathVariable Long jobId){
        return workerSkillSer.findWorkerByJobId(jobId);
    }

    @GetMapping("/clientsFilter")
    public List<Client> clientsFilter(@RequestBody FilterClientDTO filterClientDTO){
        return clientSer.clientCriteria(filterClientDTO);
    }

    @GetMapping("/workersFilter")
    public  List<Worker> workersFilter(@RequestBody FilterWorkerDTO filterWorkerDTO){
        return workerSer.workerCriteria(filterWorkerDTO);
    }
}
