package ir.maktab.homeservicecompany.models.bank_card.service;

import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.bank_card.entity.BankCard;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;

public interface BankCardService extends BaseService<BankCard> {
    BankCard findByCardNumber(String cardNumber);

    BankCard moneyTransfer(MoneyTransferDTO moneyTransferDTO);
}
