package ir.maktab.homeservicecompany.models.client.service;

import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.offer.dto.ChooseOfferDTO;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.client.dto.FilterClientDTO;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.utils.dto.PasswordDTO;
import ir.maktab.homeservicecompany.utils.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService extends BaseService<Client> {
    Optional<Client> findByEmail(String email);

    void signUp(UserDTO userDTO);

    void changePassword(PasswordDTO passwordDTO);
    void setRequestStatusOnStarted(Long clientId, Long requestId);
    void setRequestStatusOnCompleted(Long clientId, Long requestId);

    void chooseAnOffer(ChooseOfferDTO chooseOfferDTO);

    void payWithCredit(Long clientId, Long requestId);

    List<Client> clientCriteria(FilterClientDTO filterClientDto);

    void increaseCredit(Long clientId, MoneyTransferDTO moneyTransferDTO);

}