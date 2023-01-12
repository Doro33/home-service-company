package ir.maktab.homeservicecompany.models.offer.dao;

import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferDao extends JpaRepository<Offer,Long> {
    List<Offer> findByRequestOrderByExpectedPrice(Request request);
    @Query("""
           select o from Offer as o
           where o.request.id = :requestId
           order by o.worker.score
""")
    List<Offer> findByRequestOrderByWorkerScore(Long requestId);

    boolean existsByWorkerAndRequest(Worker worker, Request request);
}