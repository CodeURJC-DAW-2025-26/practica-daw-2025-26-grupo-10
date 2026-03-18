package es.tickethub.tickethub.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import es.tickethub.tickethub.entities.Event;
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
        model.addAttribute("categories", eventService.getUniqueCategories());
        model.addAttribute("events", eventService.getFirstPageOfEvents().getContent());
        return "/public/events";
    }

    @GetMapping("/public/event/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("event", eventService.findByIdOrThrow(id));
            return "public/event";
        } catch (ResponseStatusException e) {
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

        Page<Event> eventPage = eventService.searchEvents(artist, category, date, page, 5);

        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("hasMore", eventPage.hasNext());

        return "fragments/eventsFragments";
    }

    @GetMapping("/public/purchase/{eventID}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model) {
        try {
            Event event = eventService.findByIdOrThrow(eventID);
            
            model.addAttribute("event", event);
            model.addAttribute("zones", zoneService.findAll());
            model.addAttribute("discounts", event.getDiscounts());
            model.addAttribute("ticketCounts", List.of(1, 2, 3, 4, 5));
            model.addAttribute("totalPrice", BigDecimal.ZERO);

            return "public/purchase";
        } catch (ResponseStatusException e) {
            return "redirect:/public/events";
        }
    }

    @GetMapping("/public/confirmation/{eventID}")
    public String showConfirmation(@PathVariable Long eventID, Model model) {
        try {
            model.addAttribute("event", eventService.findByIdOrThrow(eventID));
            return "public/confirmation";
        } catch (ResponseStatusException e) {
            return "redirect:/public/events";
        }
    }

}