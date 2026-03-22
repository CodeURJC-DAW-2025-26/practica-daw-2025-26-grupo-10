package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.SessionCreateDTO;
import es.tickethub.tickethub.dto.SessionDTO;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.mappers.SessionMapper;
import es.tickethub.tickethub.services.SessionService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionMapper sessionMapper;


    @GetMapping("/upcoming")
    public List<SessionDTO> getUpcomingSessions() {
        return sessionMapper.toDTOs(sessionService.getSessionsFromNow());
    }

    @GetMapping("/date/{date}")
    public List<SessionDTO> getSessionsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
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
    public ResponseEntity<SessionDTO> createSession(@Valid @RequestBody SessionCreateDTO sessionDTO) {
        Session newSession = sessionService.createSession(sessionDTO.eventID(), sessionDTO.dateStr());
        URI location = fromCurrentRequest().path("/{sessionID}").buildAndExpand(newSession.getSessionID()).toUri();
        return ResponseEntity.created(location).body(sessionMapper.toDTO(newSession));
    }

    @PutMapping("/{sessionID}")
    public SessionDTO updateSession(@PathVariable Long sessionID, @Valid @RequestBody SessionCreateDTO sessionDTO) {
        Session updatedSession = sessionService.updateSession(sessionID, sessionDTO.dateStr());
        return sessionMapper.toDTO(updatedSession);
    }

    @DeleteMapping("/{sessionID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Long sessionID) {
        sessionService.deleteSession(sessionID);
    }
}