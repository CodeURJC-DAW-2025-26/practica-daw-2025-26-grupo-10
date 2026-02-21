package es.tickethub.tickethub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Event;


public interface EventRepository extends JpaRepository <Event,Long>{
    Optional <Event> findByName(String name);
}
