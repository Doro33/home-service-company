package ir.maktab.homeservicecompany.models.worker.controller;

import ir.maktab.homeservicecompany.models.offer.dto.OfferDTO;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.offer.service.OfferService;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.models.worker_skill.dto.WorkerSkillDTO;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerSer;
    private final OfferService offerSer;
    private final Validation validation;

    @PostMapping("/signup")
    @ResponseBody
    void signUp(@RequestBody WorkerDto workerDTO, @RequestParam("image") MultipartFile image) {
        validation.imageValidate(image);
        try {
            workerSer.signUp(
                    new Worker(
                            workerDTO.getFirstName(),
                            workerDTO.getLastName(),
                            workerDTO.getEmail(),
                            workerDTO.getPassword(),
                            image.getBytes()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("image cannot be save.");
        }
    }

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
    private Long showWorkerScore(@PathVariable Long workerId){
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
}
