package ir.maktab.homeservicecompany.models.offer.service;

import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.request.entity.Request;


import java.util.List;

public interface OfferService extends BaseService<Offer> {
    Offer saveNewOffer(Offer offer);

    List<Offer> findByRequestOrderByExpectedPrice(Request request);

    List<Offer> findByRequestOrderByWorkerScore(Long requestId);
}