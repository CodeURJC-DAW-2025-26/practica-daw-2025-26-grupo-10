package es.tickethub.tickethub.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.AdminViewService;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventManagementService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private AdminViewService adminViewService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ArtistService artistService;

    // To show the manage_events view
    @GetMapping("/manage_events")
    public String showManageEvents(Model model) {
        model.addAttribute("events", eventService.findAll());

        return "admin/events/manage_events";
    }

    // To show the create_event view
    @GetMapping("/create_event")
    public String showCreateForm(Model model) {
        List<Artist> allArtists = artistService.findAll();
        List<Zone> allZones = zoneService.findAll();
        List<Discount> allDiscounts = discountService.getAllDiscounts();
        model.addAttribute("allArtists", allArtists);
        model.addAttribute("allZones", allZones);
        model.addAttribute("allDiscounts", allDiscounts);
        return "admin/events/create_event";
    }

    // To create a new event
    @PostMapping("/create_event")
    public String createEvent(@Valid Event event, BindingResult result, @RequestParam Long artistID,
            @RequestParam("images") MultipartFile[] files, Model model) {

        if (result.hasErrors()) {
            return "/admin/events/create_event";
        }
        try {
            eventService.create(event, artistID, files);
        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/events/create_event";
        }
        return "redirect:/admin/events/manage_events";
    }

    // To show the edit event view
    @GetMapping("/edit_event/{eventID}")
    public String editEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findByIdOrThrow(eventID);

        adminViewService.prepareEditEventView(model, event);

        return "admin/events/create_event";
    }

    // To edit a event saved in the database
    @PostMapping("/edit_event/{eventID}")
    public String updateEvent(@Valid Event event, BindingResult result, @RequestParam Long artistID,
            @RequestParam("discounts") List<Long> discountIDs, @RequestParam("images") MultipartFile[] files, Model model) {

        if (result.hasErrors()) {
            return "/admin/events/create_event";
        }

        try {
            Event existing = eventService.findById(event.getEventID()).get();

            eventService.edit(existing, event, artistID, discountIDs, files);

        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/artists/create_artist";
        }
        return "redirect:/admin/events/manage_events";
    }

    // To delete a saved event
    @DeleteMapping("/delete_event/{eventID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventID) {
        eventService.deleteEvent(eventID);
    }

}
