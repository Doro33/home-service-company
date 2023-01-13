package ir.maktab.homeservicecompany.models.bank_card.service;

import ir.maktab.homeservicecompany.models.bank_card.dao.BankCardDao;
import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.bank_card.entity.BankCard;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.utils.exception.CreditAmountException;
import org.springframework.stereotype.Service;

@Service
public class BankCardServiceImpl extends BaseServiceImpl<BankCard, BankCardDao> implements BankCardService {
    public BankCardServiceImpl(BankCardDao repository) {
        super(repository);
    }

    @Override
    public BankCard findByCardNumber(String cardNumber) {
        return repository.findByCardNumber(cardNumber).orElseThrow(
                ()->new IllegalArgumentException("card number is not valid."));
    }

    @Override
    public BankCard moneyTransfer(MoneyTransferDTO cardDTO) {
        BankCard card = findByCardNumber(cardDTO.getCardNumber());
        Double amount = cardDTO.getAmount();
        if (amount==null)
            throw new IllegalArgumentException("money transfer amount cannot be null.");
        if (amount<=0)
            throw new IllegalArgumentException("money transfer amount must be positive.");
        if (!cardDTO.getPassword().matches(card.getPassword()) )
            throw new IllegalArgumentException("incorrect password.");
        if (cardDTO.getCvv2()!= card.getCvv2())
            throw new IllegalArgumentException("incorrect cvv2");
        if (cardDTO.getExpYear()!=card.getExpYear())
            throw new IllegalArgumentException("incorrect exp year.");
        if (cardDTO.getExpMonth()!= card.getExpMonth())
            throw new IllegalArgumentException("incorrect exp month.");
        if (card.getCredit()<amount)
            throw new CreditAmountException("bank card's credit is not enough.");

        card.setCredit(card.getCredit()-amount);
        return saveOrUpdate(card);
    }
}
