package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.worker.dto.FilterWorkerDTO;
import ir.maktab.homeservicecompany.utils.dto.UserDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;

import java.util.List;
import java.util.Optional;

public interface WorkerService extends BaseService<Worker> {

    Optional<Worker> findByEmail(String email);

    void changePassword(PasswordDTO passwordDTO);


    void signUp(UserDTO userDTO, byte[] image);

    void confirmWorker(Long id);

    List<Worker> workerCriteria(FilterWorkerDTO filterWorkerDTO);

    void addSkill(Long workerId,Long jobId);

}
