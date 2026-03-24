package es.tickethub.tickethub.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

        Slice<Purchase> findByClient_Email(String email, Pageable pageable);

        List<Purchase> findBySession(Session session);

        Slice<Purchase> findByClient_UserID(Long clientID, Pageable pageable);

        Optional<Purchase> findByPurchaseIDAndClient(Long id, Client client);

        @Query(value = "SELECT MONTHNAME(s.date), e.name, " +
                        "SUM((SELECT COUNT(*) FROM ticket t WHERE p.purchaseid = t.purchase_id)) " +
                        "FROM purchase p " +
                        "JOIN session s ON s.sessionid = p.session_id " +
                        "JOIN event e ON e.event_id = s.event_id " +
                        "GROUP BY MONTHNAME(s.date), e.name, MONTH(s.date) " +
                        "ORDER BY MONTH(s.date)", nativeQuery = true)
        List<Object[]> getTicketsByMonthAndEvent();;

        @Query("SELECT p.session.event.name, SUM(SIZE(p.tickets)) "
                        + "FROM Purchase p GROUP BY p.session.event.name ORDER BY SUM(SIZE(p.tickets)) DESC")
        List<Object[]> getRankingByEvent();

        @Query(value = "SELECT MONTHNAME(s.date), " +
                        "SUM((SELECT COUNT(*) FROM ticket t WHERE p.purchaseid = t.purchase_id)) " +
                        "FROM purchase p " +
                        "JOIN session s ON s.sessionid = p.session_id " +
                        "GROUP BY MONTHNAME(s.date), MONTH(s.date) " +
                        "ORDER BY MONTH(s.date)", nativeQuery = true)
        List<Object[]> getTotalTicketsEvolution();
}