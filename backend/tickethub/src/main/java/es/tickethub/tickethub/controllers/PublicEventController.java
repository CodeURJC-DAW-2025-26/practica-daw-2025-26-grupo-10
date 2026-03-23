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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.PurchaseService;
import es.tickethub.tickethub.services.ZoneService;

@Controller
@RequestMapping("/public")
public class PublicEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("categories", eventService.getUniqueCategories());
        model.addAttribute("events", eventService.getFirstPageOfEvents().getContent());
        return "/public/events";
    }

    @GetMapping("/events/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("event", eventService.findByIdOrThrow(id));
            return "public/event";
        } catch (ResponseStatusException e) {
            return "redirect:/public/events";
        }
    }

    @GetMapping("/events/fragments")
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

    @GetMapping("/purchase/{eventID}")
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

    @GetMapping("/confirmation/{purchaseID}")
    public String showConfirmation(@PathVariable Long purchaseID, Model model) {
        try {
            Purchase purchase = purchaseService.getPurchaseById(purchaseID);
            Event event = purchase.getSession().getEvent();
            model.addAttribute("purchase", purchase);
            model.addAttribute("event", event);
            return "public/confirmation";
        } catch (ResponseStatusException e) {
            return "redirect:/public/events";
        }
    }

}