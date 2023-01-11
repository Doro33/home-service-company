package ir.maktab.homeservicecompany.models.request.service;

import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.request.entity.Request;

public interface RequestService extends BaseService<Request> {
    Request saveNewRequest(Request request);
}
