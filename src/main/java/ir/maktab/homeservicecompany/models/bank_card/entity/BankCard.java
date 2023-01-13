package ir.maktab.homeservicecompany.models.bank_card.entity;

import ir.maktab.homeservicecompany.utils.AppContext;
import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BankCard extends BaseEntity {
    private String cardNumber;
    private short cvv2;
    private String password;
    private int expMonth;
    private int expYear;
    private double credit;

    public void setCardNumber(String cardNumber) {
        if (!cardNumber.matches("^\\d{16}$") || cardNumber.matches("^\\d{12}$"))
            throw new RuntimeException("This card number is not valid.");
        this.cardNumber = cardNumber;
    }

    public void setPassword(String password) {
        if (!password.matches("^\\d{4}$"))
            throw new IllegalArgumentException("This password is not valid.");
        this.password = password;
    }

    public BankCard(String cardNumber, String password, double credit) {
        setCardNumber(cardNumber);
        setPassword(password);
        this.cvv2 =(short) AppContext.getRANDOM().nextInt(100,10000);
        this.expYear = LocalDate.now().getYear()+4;
        this.expMonth = LocalDate.now().getMonthValue();
        this.credit = credit;
    }
}
