package es.tickethub.tickethub.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByClientEmail(String email);
    Slice<Purchase> findByClient_UserID(Long clientID,Pageable pageable);

    Optional<Purchase> findByPurchaseIDAndClient(Long id, Client client);
}

