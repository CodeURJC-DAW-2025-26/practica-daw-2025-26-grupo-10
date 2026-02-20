package es.tickethub.tickethub.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.repositories.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    //Retrieves all sessions for a given event.
    public List<Session> getSessionByEvent(Long eventID){
        return validateList(sessionRepository.findByEvent_EventID(eventID));
    }

    //Retrieves all active sessions from the current timestamp onwards.
    public List<Session> getSessionsFromNow() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return validateList(sessionRepository.findByDateAfter(now));
    } 

    /**
     * Retrieves all sessions within a full day (00:00 to 23:59:59.999).
     * If the date is today, starts from current timestamp.
     */
    public List<Session> getSessionsByFullDay(LocalDate date) {
        Timestamp end = Timestamp.valueOf(date.atTime(LocalTime.MAX));
        if (date.isEqual(LocalDate.now())){
            Timestamp startNow = Timestamp.valueOf(LocalDateTime.now());
            return validateList(sessionRepository.findByDateBetween(startNow, end));
        } else {
            Timestamp start = Timestamp.valueOf(date.atStartOfDay());
            return validateList(sessionRepository.findByDateBetween(start, end));
        }
    }

    /**
     * Helper method to ensure a non-empty list.
     * Throws NO_CONTENT if the list is empty.
     */
    private List<Session> validateList(List<Session> sessions) {
        if (sessions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No sessions found");
        }
        return sessions;
    }

    /**
     * Retrieves all purchases associated with a session.
     * Throws NO_CONTENT if no purchases exist.
     */
    public List<Purchase> getPurchases(Session session) {
        if (session.getPurchases().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No purchases for this session");
        }
        return session.getPurchases();
    }


    //Counts the total number of tickets sold in a session.
    public int countTicketsSold(Session session) {
        return session.getPurchases().stream().mapToInt(p -> p.getTickets().size()).sum();
    }

    /**
     * Checks if a session has available capacity.
     * Returns true if tickets sold are less than the event's capacity.
     */
    public boolean hasAvailableCapacity(Session session) {
        int ticketsSold = countTicketsSold(session);
        int capacity = session.getEvent().getCapacity();
        return ticketsSold < capacity;
    }
}
