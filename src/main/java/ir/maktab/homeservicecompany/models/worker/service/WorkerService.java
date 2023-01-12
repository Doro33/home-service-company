package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;

import java.util.List;

public interface WorkerService extends BaseService<Worker> {

    Worker findByEmail(String email);

    Worker changePassword(PasswordDTO passwordDTO);


    Worker signUp(Worker worker);

    Worker confirmWorker(Long id);

    List<Worker> workerCriteria(WorkerDto workerDto);

    WorkerSkill addSkill(Long workerId,Long jobId);

}
