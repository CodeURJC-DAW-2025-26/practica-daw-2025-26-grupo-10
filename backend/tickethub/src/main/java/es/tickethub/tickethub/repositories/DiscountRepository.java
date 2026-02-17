package es.tickethub.tickethub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import es.tickethub.tickethub.entities.Discount;
import jakarta.transaction.Transactional;


public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> getByDiscountName(String discountName);
    
    boolean existsByDiscountName(String discountName);

    @Transactional
    @Modifying
    void deleteByDiscountName(String discountName);
}
