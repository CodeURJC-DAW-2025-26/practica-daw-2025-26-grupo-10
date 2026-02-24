package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.EventService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @GetMapping("/admin")
    public String getAdmin() {

        return "admin/admin";
    }
    
    @GetMapping("/statistics")
    public String getStatistics() {

        return "admin/statistics";
    }

    @GetMapping("/events/manage_events")
    public String showManageEvents(Model model) {

        model.addAttribute("events", eventService.findAll());

        return "admin/events/manage_events";
    }

    @GetMapping("/events/create_event")
    public String createNewEvent(Model model) {

        model.addAttribute("event", new Event());
        
        return "admin/events/create_event";
    }

    @GetMapping("/events/edit_event/{eventID}")
    public String editEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);

        model.addAttribute("event", event);

        return "admin/events/create_event";
    }
    
}
