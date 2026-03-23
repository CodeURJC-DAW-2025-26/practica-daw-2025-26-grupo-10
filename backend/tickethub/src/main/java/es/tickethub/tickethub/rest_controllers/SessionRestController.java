package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.SessionCreateDTO;
import es.tickethub.tickethub.dto.SessionDTO;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.mappers.SessionMapper;
import es.tickethub.tickethub.services.SessionService;
import es.tickethub.tickethub.services.EventService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1")
public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EventService eventService;

    @Autowired
    private SessionMapper sessionMapper;


    @GetMapping("/public/sessions/upcoming")
    public List<SessionDTO> getUpcomingSessions() {
        return sessionMapper.toDTOs(sessionService.getSessionsFromNow());
    }

    @GetMapping("/public/sessions/date/{stringDate}")
    public List<SessionDTO> getSessionsByDate(@PathVariable String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDate date = LocalDateTime.parse(stringDate, formatter).toLocalDate();
        return sessionMapper.toDTOs(sessionService.getSessionsByFullDay(date));
    }

    @GetMapping("/public/sessions/event/{eventID}")
    public List<SessionDTO> getSessionsByEvent(@PathVariable Long eventID) {
        return sessionMapper.toDTOs(sessionService.getSessionByEvent(eventID));
    }

    @GetMapping("/admin/sessions/{sessionID}")
    public SessionDTO getSessionById(@PathVariable Long sessionID) {
        return sessionMapper.toDTO(sessionService.findById(sessionID));
    }

    @PostMapping("/admin/events/{eventID}/sessions")
    public ResponseEntity<SessionDTO> createSession(@PathVariable Long eventID, @Valid @RequestBody SessionCreateDTO sessionDTO) {
        Session newSession = sessionService.createSession(eventID, sessionDTO.dateStr());
        URI location = fromCurrentRequest().path("/{sessionID}").buildAndExpand(newSession.getSessionID()).toUri();
        return ResponseEntity.created(location).body(sessionMapper.toDTO(newSession));
    }

    @PutMapping("/admin/events/{eventID}/sessions/{index}")
    public SessionDTO updateSession(@PathVariable Long eventID, @PathVariable int index, @Valid @RequestBody SessionCreateDTO sessionDTO) {
        int num = index - 1;
        Long sessionID = eventService.findByIdOrThrow(eventID).getSessions().get(num).getSessionID();
        Session updatedSession = sessionService.updateSession(sessionID, sessionDTO.dateStr());
        return sessionMapper.toDTO(updatedSession);
    }

    @DeleteMapping("/admin/sessions/{sessionID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Long sessionID) {
        sessionService.deleteSession(sessionID);
    }
}