package es.tickethub.tickethub.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminEventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ArtistService artistService;

    // To show the manage_events view
    @GetMapping("/admin/events/manage_events")
    public String showManageEvents(Model model) {
        model.addAttribute("events", eventService.findAll());

        return "/admin/events/manage_events";
    }

    // To show the create_event view
    @GetMapping("/admin/events/create_event")
    public String showCreateForm(Model model) {
        List<Artist> allArtists = artistService.findAll();
        List<Zone> allZones = zoneService.findAll();
        List<Discount> allDiscounts = discountService.getAllDiscounts();
        model.addAttribute("allArtists", allArtists);
        model.addAttribute("allZones", allZones);
        model.addAttribute("allDiscounts", allDiscounts);
        return "/admin/events/create_event";
    }

    // To create a new event
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
            // checks if any file was uploaded
            if (files != null && files.length > 0) {
                event.getEventImages().addAll(convertFilesToImages(files));
            }
            // Calculate total capacity of the event by summing capacities of all zones
            // stream() to iterate over the zones
            event.setCapacity(calculateTotalCapacity(event.getZones()));
            // event.setEventImages(images);
            eventService.save(event);
        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/events/create_event";
        }
        return "redirect:/admin/events/manage_events";
    }

    // To show the edit event view
    @GetMapping("/admin/events/edit_event/{eventID}")
    public String editEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);
        List<Artist> allArtists = artistService.findAll();
        List<Zone> allZones = event.getZones();
        List<Discount> allDiscounts = discountService.getAllDiscounts();

        // To mark the correct artist as selected in the edit view
        for (Artist artist : allArtists) {
            if (event.getArtist() != null &&
                    artist.getArtistID().equals(event.getArtist().getArtistID())) {
                artist.setSelected(true);
            } else {
                artist.setSelected(false);
            }
        }

        AdminControllerHelper.addingZonesAndDiscounts(model, event, allDiscounts);
        AdminControllerHelper.addingAttributesCreateEvent(model, event, allArtists, allZones, allDiscounts);
        return "admin/events/create_event";
    }

    // To edit a event saved in the database
    @PostMapping("/admin/events/edit_event/{eventID}")
    public String updateEvent(@Valid Event event, BindingResult result, @RequestParam("artistID") Long artistID,
            @RequestParam("images") MultipartFile[] files, Model model) {

        if (result.hasErrors()) {
            return "/admin/events/create_event";
        }

        try {
            Event existing = eventService.findById(event.getEventID());

            existing.setName(event.getName());
            existing.setCapacity(calculateTotalCapacity(event.getZones()));
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
                existing.getEventImages().addAll(convertFilesToImages(files));
            }

            eventService.save(existing);

        } catch (IOException | SQLException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/artists/create_artist";
        }
        return "redirect:/admin/events/manage_events";
    }

    // To delete a saved event
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

    private List<Image> convertFilesToImages(MultipartFile[] files) throws SQLException, IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                images.add(new Image(file.getOriginalFilename(), new SerialBlob(file.getBytes())));
            }
        }
        return images;
    }

    private int calculateTotalCapacity(List<Zone> zones) {
        return zones.stream().mapToInt(Zone::getCapacity).sum();
    }
}
