package es.tickethub.tickethub.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Slice<Purchase> findByClient_Email(String email, Pageable pageable);

    Slice<Purchase> findByClient_UserID(Long clientID, Pageable pageable);

    Optional<Purchase> findByPurchaseIDAndClient(Long id, Client client);

    // Graph 1: Tickets sold by month and event
    @Query(value = "SELECT MONTHNAME(s.date), e.name, SUM((SELECT COUNT(*) FROM ticket t WHERE p.purchaseid = t.purchase_id)) "
            + "FROM purchase p " + "JOIN session s ON s.sessionid = p.session_id "
            + "JOIN event e ON e.eventid = s.event_id " + "GROUP BY MONTHNAME(s.date), e.name, MONTH(s.date) "
            + "ORDER BY MONTH(s.date)", nativeQuery = true)
    List<Object[]> getTicketsByMonthAndEvent();

    // Graph 2: Event ranking (Total tickets / event)
    @Query("SELECT p.session.event.name, SUM(SIZE(p.tickets)) "
            + "FROM Purchase p GROUP BY p.session.event.name ORDER BY SUM(SIZE(p.tickets)) DESC")
    List<Object[]> getRankingByEvent();

    // Graph 3: Evolution (Total tickets / month)
    @Query(value = "SELECT MONTHNAME(date), SUM((SELECT COUNT(*) FROM ticket t WHERE p.purchaseid = t.purchase_id)) "
            + "FROM purchase p " + "JOIN session s ON s.sessionid = p.session_id "
            + "GROUP BY MONTHNAME(date), MONTH(date) " + "ORDER BY MONTH(date)", nativeQuery = true)
    List<Object[]> getTotalTicketsEvolution();
}
