package ir.maktab.homeservicecompany.models.request.service;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.job.entity.Job;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.request.entity.Request;

import java.util.List;

public interface RequestService extends BaseService<Request> {
    Request saveNewRequest(RequestDTO requestDTO);

    List<Request> findByJob(Long jobId);

    List<Request> findByClient(Long clientId);
}
