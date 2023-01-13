package ir.maktab.homeservicecompany.models.bank_card.dto;

import lombok.Getter;

@Getter
public class MoneyTransferDTO {
    private String cardNumber;
    private Short cvv2;
    private String password;
    private Integer expMonth;
    private Integer expYear;
    private Double amount;
}
