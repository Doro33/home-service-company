package ir.maktab.homeservicecompany.models.client.dao;

import ir.maktab.homeservicecompany.models.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClientDao extends JpaRepository<Client,Long> {
    Optional<Client> findByEmail(String email);
}