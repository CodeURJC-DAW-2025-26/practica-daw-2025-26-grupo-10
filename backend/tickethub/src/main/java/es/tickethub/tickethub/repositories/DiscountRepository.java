package es.tickethub.tickethub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> getByDiscountName(String discountName);
    
}
