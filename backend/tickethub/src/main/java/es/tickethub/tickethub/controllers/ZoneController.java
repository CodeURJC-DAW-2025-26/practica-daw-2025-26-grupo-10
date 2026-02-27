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

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Ticket;
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

    // Show all zones of an event
    @GetMapping("/edit_event/{eventID}/manage_zones")
    public String listZones(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);

        model.addAttribute("zones", event.getZones());

        return "admin/events/manage_zones";
    }

    // New zone form
    @GetMapping("/edit_event/{eventID}/create_zone")
    public String showCreateForm(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);

        model.addAttribute("event", event);
        
        return "/admin/events/create_zone"; 
    }

    // Creation of new zone
    @PostMapping("/edit_event/{eventID}/create_zone")
    public String createZone(@Valid Zone zone, @PathVariable Long eventID, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "admin/events/edit_event/{eventID}";
        }

        try {
            Event event = eventService.findById(eventID);
            Zone newZone = new Zone(zone.getName(), zone.getCapacity(), zone.getPrice());
            newZone.setEvent(event);
            event.getZones().add(newZone);

            event.setCapacity(event.getZones().stream().mapToInt(Zone::getCapacity).sum());

            zoneService.save(newZone);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/events/edit_event/{eventID}";
        }

        return "redirect:/admin/events/edit_event/{eventID}/manage_zones";
    }

    @GetMapping("/edit_event/{eventID}/edit_zone/{id}")
    public String showZone(@PathVariable Long id, @PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);

        Zone zone = event.getZones().stream().filter(z -> z.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));
        
        model.addAttribute("event", event);
        model.addAttribute("zone", zone);

        return "/admin/events/create_zone";
    }

    //To save the edited zone
    @PostMapping("/edit_event/{eventID}/edit_zone/{id}")
    public String editZone(@Valid Zone zone, @PathVariable Long eventID, BindingResult result, Model model) {

        try {
            if (result.hasErrors()) {
                return "admin/events/edit_event/{eventID}/create_zone";
            }

            Event event = eventService.findById(eventID);

            Zone existing = event.getZones().stream()
                    .filter(z -> z.getId().equals(zone.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Zone not found"));

            existing.setName(zone.getName());
            existing.setCapacity(zone.getCapacity());
            existing.setPrice(zone.getPrice());
            for (Ticket ticket : existing.getTickets()) {
                ticket.setZone(existing);
            }

            // Update the event's total capacity
            event.setCapacity(event.getZones().stream().mapToInt(Zone::getCapacity).sum());

            zoneService.save(existing);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/zones/create_zone";
        }
        return "redirect:/admin/events/edit_event/{eventID}/manage_zones";
    }

    @DeleteMapping("/edit_event/{eventID}/delete_zone/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteZone(@PathVariable Long eventID, @PathVariable Long id) {
        Event event = eventService.findById(eventID);
        event.getZones().removeIf(z -> z.getId().equals(id));
        zoneService.deleteById(id);
    }
}
