package ir.maktab.homeservicecompany.models.client.service;

import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.client.dto.ClientDTO;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.client.dto.ClientFilterDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;

import java.util.List;

public interface ClientService extends BaseService<Client> {
    Client findByEmail(String email);

    Client signUp(ClientDTO clientDTO);

    Client changePassword(String email, String oldPassword, String newPassword1, String newPassword2);
    void setRequestStatusOnStarted(Long clientId, Long requestId);
    void setRequestStatusOnCompleted(Long clientId, Long requestId);

    void chooseAnOffer(ChooseOfferDTO chooseOfferDTO);

    void payWithCredit(Long clientId, Long requestId);

    List<Client> clientCriteria(ClientFilterDTO clientFilterDto);

    Client increaseCredit(Long clientId, MoneyTransferDTO moneyTransferDTO);

}