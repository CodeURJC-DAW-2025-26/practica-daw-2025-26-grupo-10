package es.tickethub.tickethub.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.DiscountService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.validation.Valid;


@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ArtistService artistService;

    /*------------------------------------- FUNCTIONS FOR THE PUBLIC FOLDER ---------------------------------*/
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

    /* To see the purchase page*/
    @GetMapping("/public/purchase/{eventID}")
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
    @GetMapping("/public/confirmation/{eventID}")
    public String showConfirmation(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        
        model.addAttribute("event", event);
        return "public/confirmation";
    }

    /*------------------------------------- FUNCTIONS FOR THE ADMIN FOLDER ---------------------------------*/
    //To show the manage_events view
    @GetMapping("/admin/events/manage_events")
    public String showManageEvents(Model model) {
        model.addAttribute("events", eventService.findAll());

        return "/admin/events/manage_events";
    }

    @GetMapping("/admin/events/{id}/manage_sessions")
    public String manageSessions(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        return "/admin/events/manage_sessions";
    }

    //To show the create_event view
    @GetMapping("/admin/events/create_event")
    public String showCreateForm(Model model) {
        List <Artist> allArtists = artistService.findAll();
        List<Zone> allZones = zoneService.findAll();
        List<Discount> allDiscounts = discountService.getAllDiscounts();
        model.addAttribute("allArtists", allArtists);
        model.addAttribute("allZones", allZones);
        model.addAttribute("allDiscounts", allDiscounts);
        return "/admin/events/create_event";
    }

    //To create a new event
    @PostMapping("/admin/events/create_event")
    public String createEvent(@Valid Event event, BindingResult result, @RequestParam("artistID") Long artistID,
                                @RequestParam("images") MultipartFile[] files, Model model) {

        if (result.hasErrors()) {
            return "/admin/events/create_event";
        }
        try {

            Artist artist = artistService.findById(artistID);
            event.setArtist(artist);
            artist.getEventsIncoming().add(event);

            List<Image> images = new ArrayList<>();
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        Blob blob = new SerialBlob(file.getBytes());
                        Image image = new Image(file.getOriginalFilename(), blob);
                        images.add(image);
                    }
                }
            }
            event.setCapacity(event.getZones().stream().mapToInt(Zone::getCapacity).sum());   //This adds the capacity of all the zones to set the total capacity of the event

            event.setEventImages(images);
            eventService.save(event);
        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/events/create_event";
        }
        return "redirect:/admin/events/manage_events";
    }

    //To show the edit event view
    @GetMapping("/admin/events/edit_event/{eventID}")
    public String editEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        List <Artist> allArtists = artistService.findAll();
        List<Zone> allZones = event.getZones();
        List<Discount> allDiscounts = discountService.getAllDiscounts();

        //To mark the correct artist as selected in the edit view
        for (Artist artist : allArtists) {
            if (event.getArtist() != null &&
                artist.getArtistID().equals(event.getArtist().getArtistID())) {
                artist.setSelected(true);
            } else {
                artist.setSelected(false);
            }
        }

        addingZonesAndDiscounts(model, event, allDiscounts);

        addingAttributesCreateEvent(model, event, allArtists, allZones, allDiscounts);
        return "admin/events/create_event";
    }

    public void addingAttributesCreateEvent(Model model, Event event, List<Artist> allArtists, List<Zone> allZones, List<Discount> allDiscounts) {
        model.addAttribute("event", event);
        model.addAttribute("allArtists", allArtists);
        model.addAttribute("allZones", allZones);
        model.addAttribute("allDiscounts", allDiscounts);

        //This is to mark the correct target age when editing an event
        model.addAttribute("targetAge0", event.getTargetAge() == 0);
        model.addAttribute("targetAge1", event.getTargetAge() == 1);
        model.addAttribute("targetAge2", event.getTargetAge() == 2);
        model.addAttribute("targetAge3", event.getTargetAge() == 3);
        model.addAttribute("targetAge4", event.getTargetAge() == 4);
        model.addAttribute("targetAge5", event.getTargetAge() == 5);
        model.addAttribute("targetAge6", event.getTargetAge() == 6);
    }

    public void addingZonesAndDiscounts(Model model, Event event, List<Discount> allDiscounts) {
        
        List<Map<String,Object>> eventDiscountsForTemplate = new ArrayList<>();

        for (Discount d : event.getDiscounts()) {
            d.setSelected(true);

            List<Map<String,Object>> allDiscountsCopy = new ArrayList<>();

            for (Discount discount : allDiscounts) {
                Map<String,Object> map = new HashMap<>();
                map.put("id", discount.getDiscountID());
                map.put("name", discount.getDiscountName());
                map.put("selected", discount.isSelected());
                allDiscountsCopy.add(map);
            }

            Map<String,Object> row = new HashMap<>();
            row.put("value", d.getAmmount());
            row.put("isPercent", d.getPercentage().equals(true));
            row.put("isAmmount", d.getPercentage().equals(false));
            row.put("allDiscounts", allDiscountsCopy);

            eventDiscountsForTemplate.add(row);
        }

        model.addAttribute("eventDiscounts", eventDiscountsForTemplate);
    }

    //To edit a event saved in the database
    @PostMapping("/admin/events/edit_event/{eventID}")
    public String updateEvent(@Valid Event event, BindingResult result, @RequestParam("artistID") Long artistID, @RequestParam("images") MultipartFile[] files, Model model) {

        if (result.hasErrors()) {
            return "/admin/events/create_event";
        }
        
        try {
            Event existing = eventService.findById(event.getEventID());

            existing.setName(event.getName());
            existing.setCapacity(event.getZones().stream().mapToInt(Zone::getCapacity).sum());  //This adds the capacity of all the zones to set the total capacity of the event
            existing.setTargetAge(event.getTargetAge());

            Artist oldArtist = existing.getArtist();
            Artist newArtist = artistService.findById(artistID);

            if (!oldArtist.getArtistID().equals(newArtist.getArtistID())) {
                oldArtist.getEventsIncoming().remove(existing);
                oldArtist.getLastEvents().remove(existing);

                newArtist.getEventsIncoming().add(existing);
                existing.setArtist(newArtist);
            }

            for (Zone zone : event.getZones()) {
                zone.setEvent(existing);
                existing.getZones().add(zone);
            }

            existing.getDiscounts().clear();
            existing.getDiscounts().addAll(event.getDiscounts());

            existing.setPlace(event.getPlace());
            existing.setCategory(event.getCategory());

            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        Blob blob = new SerialBlob(file.getBytes());
                        Image image = new Image(file.getOriginalFilename(), blob);
                        existing.getEventImages().add(image);
                    }
                }
            }

            eventService.save(existing);

        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/artists/create_artist";
        }
        return "redirect:/admin/events/manage_events";
    }

    //To delete a saved event
    @DeleteMapping("/admin/events/delete_event/{eventID}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@PathVariable Long eventID) {
        Event event = eventService.findById(eventID);
        Artist artist = event.getArtist();

        artist.getLastEvents().remove(event);
        artist.getEventsIncoming().remove(event);
            
        event.setArtist(null);
        event.setZones(null);
        event.setDiscounts(null);

        eventService.deleteById(eventID);
    }

}
