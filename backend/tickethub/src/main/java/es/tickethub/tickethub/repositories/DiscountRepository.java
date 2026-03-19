package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Discount;
import java.util.Optional;


public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByDiscountName(String discountName);
}
