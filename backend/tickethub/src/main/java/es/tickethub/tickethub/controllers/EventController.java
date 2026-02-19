package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.EventService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired EventService eventService;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "events";
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "create-event";
    }

    @PostMapping
    public String createEvent(@Valid Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-event";
        }
        try {
            eventService.save(event);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-event";
        }
        return "redirect:/events";
    }

    @GetMapping("/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        return "event";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return "redirect:/events";
    }

    @GetMapping("/manage/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        return "manage-event";
    }

    @PostMapping("/manage")
    public String updateEvent(@Valid Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "manage-event";
        }
        eventService.save(event);
        return "redirect:/events";
    }

}
