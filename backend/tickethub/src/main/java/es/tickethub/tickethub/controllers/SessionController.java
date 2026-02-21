package es.tickethub.tickethub.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.services.SessionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired SessionService sessionService;

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
}
