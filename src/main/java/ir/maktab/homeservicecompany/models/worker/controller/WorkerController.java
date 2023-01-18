package ir.maktab.homeservicecompany.models.worker.controller;

import ir.maktab.homeservicecompany.models.offer.dto.OfferDTO;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.models.worker_skill.dto.WorkerSkillDTO;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
@AllArgsConstructor
public class WorkerController {
    private final WorkerService workerSer;
    private final OfferService offerSer;

    @PutMapping("/changePassword")
    @ResponseBody
    void changePassword(@RequestBody PasswordDTO passwordDTO) {
        workerSer.changePassword(passwordDTO);
    }

    @PostMapping("/addSkill")
    @ResponseBody
    void addSkill(@RequestBody WorkerSkillDTO workerSkillDTO){
        workerSer.addSkill(workerSkillDTO.getWorkerId(), workerSkillDTO.getJobId());
    }

    @PostMapping("/addOffer")
    public void addOffer(@RequestBody OfferDTO offerDTO){
        offerSer.saveNewOffer(offerDTO);
    }

    @GetMapping("/showScore/{workerId}")
    public Long showWorkerScore(@PathVariable Long workerId){
        return workerSer.findById(workerId).getScore();
    }

    @GetMapping("/showCredit/{workerId}")
    public Double showCredit(@PathVariable Long workerId){
        return workerSer.findById(workerId).getCredit();
    }

    @GetMapping("/showMyOffers/{workerId}")
    public List<Offer> showWorkerOffers(@PathVariable Long workerId){
        return offerSer.findByWorker(workerId);
    }

    @PutMapping("/activateAccount/{workerId}")
    public void activateAccount(@PathVariable Long workerId){
        workerSer.activeWorker(workerId);
    }
}
