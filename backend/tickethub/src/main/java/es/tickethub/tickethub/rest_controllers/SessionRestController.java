package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.SessionCreateDTO;
import es.tickethub.tickethub.dto.SessionDTO;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.mappers.SessionMapper;
import es.tickethub.tickethub.services.SessionService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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


    @PostMapping("/")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionCreateDTO session) {
        Long eventID = session.eventID();
        String dateStr = session.dateStr();
        Session newSession = sessionService.createSession(eventID, dateStr);
        URI location = fromCurrentRequest().path("/{sessionID}").buildAndExpand(newSession.getSessionID()).toUri();
        return ResponseEntity.created(location).body(sessionMapper.toDTO(newSession));
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