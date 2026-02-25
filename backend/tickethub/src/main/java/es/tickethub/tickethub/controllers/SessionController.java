package es.tickethub.tickethub.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.SessionService;

@Controller
@RequestMapping("/admin/events")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EventService eventService;

    // Show upcoming sessions
    @GetMapping("/upcoming")
    public String getUpcomingSessions(Model model) {
        model.addAttribute("sessions", sessionService.getSessionsFromNow());
        return "sessions"; 
    }

    // Filter by day
    @GetMapping("/date/{date}")
    public String getSessionsByDate(
            @PathVariable 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate date,
            Model model) {

        model.addAttribute("sessions", sessionService.getSessionsByFullDay(date));
        model.addAttribute("selectedDate", date);
        return "sessions";
    }

    // Show a specific event's sessions
    @GetMapping("/event/{eventID}")
    public String getSessionsByEvent(@PathVariable Long eventID, Model model) {
        model.addAttribute("sessions", sessionService.getSessionByEvent(eventID));
        model.addAttribute("eventID", eventID);
        return "sessions"; 
    }

    @PostMapping("{eventID}/add_session")
    @ResponseBody   //To return a simple status without redirecting
    public ResponseEntity<?> addSession(@RequestParam String date, @PathVariable Long eventID) {
        Event event = eventService.findById(eventID);

        Session session = new Session();
        session.setEvent(event);
        System.out.println("Received: [" + date + "]");
        session.setDate(session.getTimestampedDate(date));

        event.getSessions().add(session);
        sessionService.save(session);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete_session/{sessionID}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSession(@PathVariable Long sessionID) {
        sessionService.deleteSession(sessionID);
    }

}
