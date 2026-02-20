package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    
}