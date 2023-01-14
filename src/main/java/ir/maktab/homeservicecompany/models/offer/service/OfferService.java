package ir.maktab.homeservicecompany.models.offer.service;

import ir.maktab.homeservicecompany.models.offer.dto.OfferDTO;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.request.entity.Request;


import java.util.List;

public interface OfferService extends BaseService<Offer> {
    void saveNewOffer(OfferDTO offerDTO);

    List<Offer> findByRequestOrderByExpectedPrice(Long requestId);

    List<Offer> findByRequestOrderByWorkerScore(Long requestId);

    List<Offer> findByWorker(Long workerId);

    boolean existsByWorkerAndRequest(Worker worker, Request request);
}