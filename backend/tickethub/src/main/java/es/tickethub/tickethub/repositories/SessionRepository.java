package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Session;

import java.sql.Timestamp;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    // It searches an attribute Event in Session then enters Event and picks the
    // EventID
    List<Session> findByEvent_EventID(Long eventID);

    // Search for a session starting now or on a specific day and time
    List<Session> findByDateAfter(Timestamp date);

    // Search for a session on a specific day regardless of the time
    List<Session> findByDateBetween(Timestamp start, Timestamp end);
}
