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
@RequestMapping("/admin/events")
public class AdminSessionController {
    @Autowired
    private EventService eventService;

    @GetMapping("/{id}/manage_sessions")
    public String manageSessions(@PathVariable Long id, Model model) {
        Event event = eventService.findByIdOrThrow(id);
        model.addAttribute("event", event);
        return "admin/events/manage_sessions";
    }
}
