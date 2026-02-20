package es.tickethub.tickethub.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByClientEmail(String email);

    Optional<Purchase> findByIdAndClient(Long id, Client client);
}

