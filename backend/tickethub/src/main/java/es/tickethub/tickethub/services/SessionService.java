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

import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.repositories.SessionRepository;

@Service
public class SessionService {
    @Autowired SessionRepository sessionRepository;

    public List<Session> getSessionByEvent(Long eventID){
        return validateList(sessionRepository.findByEvent_EventID(eventID));
    }

    // for home page -> get all active sessions
    public List<Session> getSessionsFromNow() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return validateList(sessionRepository.findByDateAfter(now));
    } 

    // for filtering by day 
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

    private List<Session> validateList(List<Session> sessions) {
        if (sessions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No se encontraron sesiones");
        }
        return sessions;
    }
}
