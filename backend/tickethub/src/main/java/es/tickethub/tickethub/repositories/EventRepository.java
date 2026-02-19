package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
}
