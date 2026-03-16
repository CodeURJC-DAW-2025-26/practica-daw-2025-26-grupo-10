package es.tickethub.tickethub.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;

@Controller
public class PublicEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @GetMapping("/public/events")
    public String events(Model model) {
        // Load the first page using the general search without filters
        List<String> categories = eventService.getUniqueCategories();
    
        model.addAttribute("categories", categories);
        model.addAttribute("events", eventService.searchEvents(null, null, null, 0, 5).getContent());
        return "/public/events";
    }

    @GetMapping("/public/event/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        Optional <Event> event = eventService.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "public/event";
        } else {
            return "redirect:/public/events";
        }
    }

    @GetMapping("/public/events/fragments")
    public String getMoreEvents(
            @RequestParam int page,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        System.out.println("DEBUG: Date received on server -> " + date);
        int size = 5;

        // This call handles artist, category, and date simultaneously
        Page<Event> eventPage = eventService.searchEvents(artist, category, date, page, size);

        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("hasMore", eventPage.hasNext());

        return "fragments/eventsFragments";
    }

    /* To see the purchase page */
    @GetMapping("/public/purchase/{eventID}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model) {
        Optional <Event> optionalEvent = eventService.findById(eventID);
        if (optionalEvent.isEmpty()) {
            return "redirect:/public/events";
        } else {
            Event event = optionalEvent.get();
            List<Zone> zones = zoneService.findAll();
            List<Discount> discounts = event.getDiscounts();

            model.addAttribute("event", event);
            model.addAttribute("zones", zones);
            model.addAttribute("discounts", discounts);
            model.addAttribute("ticketCounts", List.of(1, 2, 3, 4, 5)); // User can buy up to 5 tickets
            model.addAttribute("tickets", List.of());
            model.addAttribute("totalPrice", BigDecimal.ZERO);

            return "public/purchase";
        }
    }

    /* To see the confirmation page */
    @GetMapping("/public/confirmation/{eventID}")
    public String showConfirmation(@PathVariable Long eventID, Model model) {
        Optional<Event> optionalEvent = eventService.findById(eventID);
        if (optionalEvent.isPresent()) {
            model.addAttribute("event", optionalEvent.get());
            return "public/confirmation";
        } else {
            return "redirect:/public/events";
        }
    }
}