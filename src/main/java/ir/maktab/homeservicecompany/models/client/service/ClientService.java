package ir.maktab.homeservicecompany.models.client.service;

import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.request.entity.Request;

import java.util.List;

public interface ClientService extends BaseService<Client> {
    Client findByEmail(String email);

    Client signUp(Client client);

    Client changePassword(String email, String oldPassword, String newPassword1, String newPassword2);

    Request addRequest(Request request);

    List<Offer> findOfferByRequestOrderByPrice(Request request);
    List<Offer> findOfferByRequestOrderByScore(Request request);

    void setRequestStatusOnStarted(Request request);
    void setRequestStatusOnCompleted(Request request);

    void chooseAnOffer(Client client,Request request, Offer offer);

    void payWithCredit(Client client, Request request);

    List<Client> clientCriteria(ClientDTO clientDto);
}