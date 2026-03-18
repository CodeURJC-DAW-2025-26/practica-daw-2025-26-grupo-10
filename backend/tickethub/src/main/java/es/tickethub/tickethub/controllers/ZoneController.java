package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/events")
public class ZoneController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @GetMapping("/edit_event/{eventID}/manage_zones")
    public String listZones(@PathVariable Long eventID, Model model) {
        Event event = eventService.findByIdOrThrow(eventID);
        model.addAttribute("zones", event.getZones());
        model.addAttribute("eventID", eventID);
        return "admin/events/manage_zones";
    }

    @GetMapping("/edit_event/{eventID}/create_zone")
    public String showCreateForm(@PathVariable Long eventID, Model model) {
        Event event = eventService.findByIdOrThrow(eventID);
        model.addAttribute("event", event);
        model.addAttribute("zone", new Zone());
        return "/admin/events/create_zone";
    }

    @PostMapping("/edit_event/{eventID}/create_zone")
    public String createZone(@Valid Zone zone, BindingResult result, @PathVariable Long eventID, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("event", eventService.findByIdOrThrow(eventID));
            return "/admin/events/create_zone";
        }

        zoneService.createZone(eventID, zone);
        return "redirect:/admin/events/edit_event/" + eventID + "/manage_zones";
    }

    @GetMapping("/edit_event/{eventID}/edit_zone/{id}")
    public String showEditForm(@PathVariable Long eventID, @PathVariable Long id, Model model) {
        Event event = eventService.findByIdOrThrow(eventID);
        Zone zone = zoneService.findById(id);
        
        model.addAttribute("event", event);
        model.addAttribute("zone", zone);
        return "/admin/events/create_zone";
    }

    @PostMapping("/edit_event/{eventID}/edit_zone/{id}")
    public String editZone(@Valid Zone zone, BindingResult result, @PathVariable Long eventID, @PathVariable Long id, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("event", eventService.findByIdOrThrow(eventID));
            return "/admin/events/create_zone";
        }

        zoneService.updateZone(eventID, id, zone);
        return "redirect:/admin/events/edit_event/" + eventID + "/manage_zones";
    }

    @DeleteMapping("/edit_event/{eventID}/delete_zone/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteZone(@PathVariable Long eventID, @PathVariable Long id) {
        zoneService.deleteZone(eventID, id);
    }
}