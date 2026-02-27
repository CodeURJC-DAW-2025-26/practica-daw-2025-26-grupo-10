package es.tickethub.tickethub.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByPurchasePurchaseID(Long purchaseId);
}
