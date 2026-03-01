package es.tickethub.tickethub.controllers;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;

@Controller
public class PublicEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ArtistService artistService;

    @GetMapping("/public/events")
    public String events(Model model) {
        model.addAttribute("events", eventService.findPaginated(0, 5));
        return "/public/events";
    }

    @GetMapping("/public/event/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);

        List<Image> images = event.getEventImages();
        if (images != null && !images.isEmpty()) {
            images.get(0).setFirst(true);
        }
        model.addAttribute("event", event);
        return "public/event";
    }

    @GetMapping("/public/events/fragments")
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

    /* To see the purchase page */
    @GetMapping("/public/purchase/{eventID}")
    public String showPurchaseFromEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        List<Zone> zones = zoneService.findAll();
        List<Discount> discounts = discountService.getAllDiscounts();

        model.addAttribute("event", event);
        model.addAttribute("zones", zones);
        model.addAttribute("discounts", discounts);
        model.addAttribute("ticketCounts", List.of(1, 2, 3, 4, 5)); // the user can buy up to 5 tickets
        model.addAttribute("tickets", List.of());
        model.addAttribute("totalPrice", BigDecimal.ZERO);

        return "public/purchase";
    }

    /* To see the confirmation page */
    @GetMapping("/public/confirmation/{eventID}")
    public String showConfirmation(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);

        model.addAttribute("event", event);
        return "public/confirmation";
    }
}
