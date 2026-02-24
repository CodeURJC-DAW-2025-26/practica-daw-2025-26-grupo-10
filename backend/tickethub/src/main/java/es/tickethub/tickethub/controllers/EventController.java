package es.tickethub.tickethub.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;
import es.tickethub.tickethub.services.DiscountService;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/public")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DiscountService discountService;


    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("events", eventService.findPaginated(0, 5));
        return "/public/events";
    }

    @GetMapping("/newEvent")
    public String showCreateForm(Model model) {
        
        return "create-event";
    }

    @PostMapping
    public String createEvent(@Valid Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create_event";
        }
        try {
            eventService.save(event);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create_event";
        }
        return "redirect:/events";
    }

    @GetMapping("event/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);

        List<Image> images = event.getEventImages();
        if (images != null && !images.isEmpty()) {
            images.get(0).setFirst(true);
        }
        model.addAttribute("event", event);
        return "public/event";
    }

    /* To see the purchase page*/
    @GetMapping("/purchase/{eventID}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        List<Zone> zones = zoneService.findAll();
        List<Discount> discounts = discountService.getAllDiscounts();

        model.addAttribute("event", event);
        model.addAttribute("zones", zones);
        model.addAttribute("discounts", discounts);
        model.addAttribute("ticketCounts", List.of(1, 2, 3, 4, 5));
        model.addAttribute("tickets", List.of());
        model.addAttribute("totalPrice", BigDecimal.ZERO);

        return "public/purchase";
    }

    /* To see the confirmation page*/
    @GetMapping("/confirmation/{eventID}")
    public String showConfirmation(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        
        model.addAttribute("event", event);
        return "public/confirmation";
    }
    

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return "redirect:/events";
    }

    @PostMapping("/manage")
    public String updateEvent(@Valid Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "manage-event";
        }
        eventService.save(event);
        return "redirect:/events";
    }

    @GetMapping("/events/fragments")
    public String getMoreEvents(
            @RequestParam int page,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String category,
            Model model) {

        int size = 5;
        List<Event> events;
        boolean hasMore;

        // filter
        if ((artist != null && !artist.isEmpty()) || (category != null && !category.isEmpty())) {
            Page<Event> eventPage = eventService.searchEvents(artist, category, page, size);
            events = eventPage.getContent();
            hasMore = eventPage.hasNext();
        } else {
            // paginated
            events = eventService.findPaginated(page, size);
            hasMore = events.size() == size; 
        }

        model.addAttribute("events", events);
        model.addAttribute("hasMore", hasMore);

        return "fragments/eventsFragments";
    } 

}
