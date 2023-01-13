package ir.maktab.homeservicecompany.models.bank_card.dao;

import ir.maktab.homeservicecompany.models.bank_card.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCardDao extends JpaRepository<BankCard, Long> {
    Optional<BankCard> findByCardNumber(String cardNumber);
}
