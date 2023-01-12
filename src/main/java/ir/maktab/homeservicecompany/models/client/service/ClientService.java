package ir.maktab.homeservicecompany.models.client.service;

import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.models.request.dto.RequestDTO;
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

    List<Offer> findOfferByRequestOrderByPrice(Request request);
    List<Offer> findOfferByRequestOrderByScore(Request request);

    void setRequestStatusOnStarted(Long clientId, Long requestId);
    void setRequestStatusOnCompleted(Long clientId, Long requestId);

    void chooseAnOffer(ChooseOfferDTO chooseOfferDTO);

    void payWithCredit(Client client, Request request);

    List<Client> clientCriteria(ClientDTO clientDto);
}