package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Event;
import java.util.Optional;


public interface EventRepository extends JpaRepository <Event,Long>{
    Optional <Event> findByEventName(String name);
}
