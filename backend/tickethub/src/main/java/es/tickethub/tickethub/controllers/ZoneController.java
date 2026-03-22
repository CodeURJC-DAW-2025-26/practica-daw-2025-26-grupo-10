package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/events")
public class ZoneController {

    @Autowired private EventService eventService;

    @Autowired private ZoneService zoneService;

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

        return "admin/events/create_zone";
    }

    @PostMapping("/edit_event/{eventID}/create_zone")
    public String createZone(@Valid Zone zone, BindingResult result, @PathVariable Long eventID, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("event", eventService.findByIdOrThrow(eventID));
            return "/admin/events/create_zone";
        }

        try {
            zoneService.createAndAssignEvent(zone, eventID);
        } catch (ResponseStatusException e) {
            return "redirect:/admin/events";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/events/edit_event/{eventID}";
        }

        return "redirect:/admin/events/edit_event/{eventID}/manage_zones";
    }

    @GetMapping("/edit_event/{eventID}/edit_zone/{id}")
    public String showZone(@PathVariable Long id, @PathVariable Long eventID, Model model) {
        Event event = eventService.findByIdOrThrow(eventID);

        Zone zone = zoneService.findByEventAndID(eventID, id);

        model.addAttribute("event", event);
        model.addAttribute("zone", zone);
        return "/admin/events/create_zone";
    }

    @PostMapping("/edit_event/{eventID}/edit_zone/{id}")
    public String editZone(@Valid Zone zone, @PathVariable Long id, @PathVariable Long eventID, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/events/edit_event/" + eventID + "/create_zone";
        }

        try {
            zoneService.updateZone(eventID, id, zone);

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/events/create_zone";
        }

        zoneService.updateZone(eventID, id, zone);
        return "redirect:/admin/events/edit_event/" + eventID + "/manage_zones";
    }

    @DeleteMapping("/edit_event/{eventID}/delete_zone/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteZone(@PathVariable Long eventID, @PathVariable Long id) {
        zoneService.deleteZoneFromEvent(eventID, id);
    }
}