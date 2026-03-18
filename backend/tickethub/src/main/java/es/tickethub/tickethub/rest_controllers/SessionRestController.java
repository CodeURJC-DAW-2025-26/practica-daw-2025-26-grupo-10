package es.tickethub.tickethub.rest_controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.SessionDTO;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.mappers.SessionMapper;
import es.tickethub.tickethub.services.SessionService;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionMapper sessionMapper;


    @GetMapping("/upcoming")
    public List<SessionDTO> getUpcomingSessions() {
        List<Session> sessions = sessionService.getSessionsFromNow();
        return sessionMapper.toDTOs(sessions);
    }

    @GetMapping("/date/{date}")
    public List<SessionDTO> getSessionsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return sessionMapper.toDTOs(sessionService.getSessionsByFullDay(date));
    }

    @GetMapping("/event/{eventID}")
    public List<SessionDTO> getSessionsByEvent(@PathVariable Long eventID) {
        return sessionMapper.toDTOs(sessionService.getSessionByEvent(eventID));
    }

    @GetMapping("/{sessionID}")
    public SessionDTO getSessionById(@PathVariable Long sessionID) {
        return sessionMapper.toDTO (sessionService.findById(sessionID));
    }


    @PostMapping("/event/{eventID}")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDTO createSession(@PathVariable Long eventID, @RequestParam String date) {
        Session newSession = sessionService.createSession(eventID, date);
        return sessionMapper.toDTO(newSession);
    }

    @PutMapping("/{sessionID}")
    public SessionDTO updateSession(@PathVariable Long sessionID, @RequestParam String date) {
        Session updatedSession = sessionService.updateSession(sessionID, date);
        return sessionMapper.toDTO(updatedSession);
    }

    @DeleteMapping("/{sessionID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Long sessionID) {
        sessionService.deleteSession(sessionID);
    }
}