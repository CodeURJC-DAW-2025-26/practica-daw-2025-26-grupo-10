package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Discount;


public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
