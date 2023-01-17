package ir.maktab.homeservicecompany.utils.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.models.category.service.CategoryService;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.job.service.JobService;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.request.service.RequestService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import ir.maktab.homeservicecompany.utils.dto.UserDTO;
import ir.maktab.homeservicecompany.utils.exception.SaveImageException;
import ir.maktab.homeservicecompany.utils.validation.Validation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/general")
@AllArgsConstructor
public class GeneralController {
    private final CategoryService categorySer;
    private final JobService jobSer;
    private final ClientService clientSer;
    private final RequestService requestSer;
    private final WorkerService workerSer;
    private final Validation validation;

    @GetMapping("/findAllCategories")
    public List<Category> findAllCategories() {
        return categorySer.findAll();
    }

    @GetMapping("/findAllJobs")
    public List<Job> findAllJob() {
        return jobSer.findAll();
    }

    @PostMapping("/clientSignup")
    public void clientSignUp(@Valid @RequestBody UserDTO userDTO) {
        clientSer.signUp(userDTO);
    }

    @PostMapping("/workerSignup")
    public void workerSignUp(@Valid @RequestParam("userDTO") String jsonUserDTO, @RequestParam("image") MultipartFile image) {
        validation.imageValidate(image);
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO;
        try {
            userDTO = objectMapper.readValue(jsonUserDTO, UserDTO.class);
            workerSer.signUp(userDTO, image.getBytes());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("request param could not mapped to json.");
        } catch (IOException e) {
            throw new SaveImageException("image cannot be save.");
        }
    }

    @GetMapping("/findRequestsByJob/{id}")
    public List<Request> findRequestsByJobId(@PathVariable Long id){
        return requestSer.findByJob(id);
    }
}
