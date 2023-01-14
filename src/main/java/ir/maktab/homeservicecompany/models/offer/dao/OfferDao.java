package ir.maktab.homeservicecompany.models.offer.dao;

import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.models.offer.entity.Offer;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OfferDao extends JpaRepository<Offer,Long> {
    @Query("""
           select o from Offer as o
           where o.request.id = :requestId
           and  o.request.acceptedOffer is null
           order by o.expectedPrice
""")
    List<Offer> findByRequestOrderByExpectedPrice(Long requestId);
    @Query("""
           select o from Offer as o
           where o.request.id = :requestId
           and  o.request.acceptedOffer is null
           order by o.worker.score
""")
    List<Offer> findByRequestOrderByWorkerScore(Long requestId);

    List<Offer> findByWorker(Worker worker);

    boolean existsByWorkerAndRequest(Worker worker, Request request);
}