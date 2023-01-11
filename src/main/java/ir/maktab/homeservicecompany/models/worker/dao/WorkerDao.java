package ir.maktab.homeservicecompany.models.worker.dao;

import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface WorkerDao extends JpaRepository<Worker,Long> {
    Optional<Worker> findByEmail(String email);
}