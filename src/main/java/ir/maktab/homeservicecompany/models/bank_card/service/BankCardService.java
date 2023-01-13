package ir.maktab.homeservicecompany.models.bank_card.service;

import ir.maktab.homeservicecompany.models.bank_card.dto.BankCardDTO;
import ir.maktab.homeservicecompany.models.bank_card.entity.BankCard;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;

public interface BankCardService extends BaseService<BankCard> {
    BankCard findByCardNumber(String cardNumber);

    BankCard moneyTransfer(BankCardDTO bankCardDTO, double amount);
}
