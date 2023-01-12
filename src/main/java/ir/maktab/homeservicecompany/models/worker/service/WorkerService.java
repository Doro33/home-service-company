package ir.maktab.homeservicecompany.models.worker.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.worker_skill.entity.WorkerSkill;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.worker.dto.WorkerDto;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;

import java.util.List;

public interface WorkerService extends BaseService<Worker> {

    Worker findByEmail(String email);

    Worker changePassword(String email, String oldPassword, String newPassword1, String newPassword2);


    Worker signUp(Worker worker);

    Worker confirmWorker(Long id);
    Offer addOffer(Offer offer);

    List<Worker> workerCriteria(WorkerDto workerDto);

    WorkerSkill addSkill(Long workerId,Long jobId);

}
