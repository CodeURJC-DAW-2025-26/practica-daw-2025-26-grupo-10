package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    
}
