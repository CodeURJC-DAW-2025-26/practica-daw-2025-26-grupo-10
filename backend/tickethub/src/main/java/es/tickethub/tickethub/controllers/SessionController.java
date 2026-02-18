package es.tickethub.tickethub.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.services.SessionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired SessionService sessionService;

    // Show all upcoming sessions at Home page
    @GetMapping("/upcoming")
    public List<Session> getUpcomingSessions() {
        return sessionService.getSessionsFromNow();
    }

    // Filter by day
    @GetMapping("/date/{date}")
    public List<Session> getSessionsByDate(@PathVariable @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return sessionService.getSessionsByFullDay(date);
    }
    
    // Show sessions for an event
    @GetMapping("/event/{eventID}")
    public List<Session> getSessionsByEvent(@PathVariable Long eventID) {
        return sessionService.getSessionByEvent(eventID);
    }
    
}
