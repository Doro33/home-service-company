package ir.maktab.homeservicecompany.models.bank_card.service;

import ir.maktab.homeservicecompany.models.bank_card.dao.BankCardDao;
import ir.maktab.homeservicecompany.models.bank_card.dto.MoneyTransferDTO;
import ir.maktab.homeservicecompany.models.bank_card.entity.BankCard;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import ir.maktab.homeservicecompany.utils.exception.BankCardInfoException;
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
    public void moneyTransfer(MoneyTransferDTO cardDTO) {
        BankCard card = findByCardNumber(cardDTO.getCardNumber());
        Double amount = cardDTO.getAmount();
        double cardCredit = card.getCredit();
        checkAmount(amount);
        checkBankCardInfo(cardDTO, card);

        if (cardCredit<amount)
            throw new CreditAmountException("bank card's credit is not enough.");

        card.setCredit(cardCredit-amount);
        saveOrUpdate(card);
    }

    private static void checkBankCardInfo(MoneyTransferDTO cardDTO, BankCard card) {
        if (!(cardDTO.getPassword().matches(card.getPassword()) &&
                cardDTO.getCvv2()== card.getCvv2() &&
                cardDTO.getExpYear()== card.getExpYear() &&
                cardDTO.getExpMonth()== card.getExpMonth()))
            throw new BankCardInfoException("incorrect bank cad info");
    }

    private static void checkAmount(Double amount) {
        if (amount ==null)
            throw new IllegalArgumentException("money transfer amount cannot be null.");
        if (amount <=0)
            throw new IllegalArgumentException("money transfer amount must be positive.");
    }
}
