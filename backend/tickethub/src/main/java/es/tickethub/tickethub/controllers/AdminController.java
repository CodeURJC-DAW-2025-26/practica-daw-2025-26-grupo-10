package es.tickethub.tickethub.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.repositories.PurchaseRepository;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.EventService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping("")
    public String getAdmin() {
        return "admin/admin";
    }
    
    @GetMapping("/statistics")
    public String getStatistics(Model model) {
        // Data Graph 1 (Matrix Month/Event/Quantity)
        model.addAttribute("monthEventData", purchaseRepository.getTicketsByMonthAndEvent());

        // Data Graph 2 (Ranking)
        List<Object[]> ranking = purchaseRepository.getRankingByEvent();
        model.addAttribute("rankingLabels", ranking.stream().map(d -> d[0]).collect(Collectors.toList()));
        model.addAttribute("rankingValues", ranking.stream().map(d -> d[1]).collect(Collectors.toList()));

        // Data Graph 3 (Evolution)
        List<Object[]> evolution = purchaseRepository.getTotalTicketsEvolution();
        model.addAttribute("evolutionLabels", evolution.stream().map(d -> d[0]).collect(Collectors.toList()));
        model.addAttribute("evolutionValues", evolution.stream().map(d -> d[1]).collect(Collectors.toList()));

        return "admin/statistics";
    }

    @GetMapping("/events/manage_events")
    public String showManageEvents(Model model) {

        model.addAttribute("events", eventService.findAll());

        return "admin/events/manage_events";
    }

    @GetMapping("/events/create_event")
    public String createNewEvent(Model model) {

        model.addAttribute("event", new Event());
        
        return "admin/events/create_event";
    }

    @GetMapping("/events/edit_event/{eventID}")
    public String editEvent(@PathVariable Long eventID, Model model) {
        Event event = eventService.findById(eventID);

        model.addAttribute("event", event);

        return "admin/events/create_event";
    }
    
    @GetMapping("/artists/manage_artists")
    public String showManageArtists(Model model) {

        model.addAttribute("artists", artistService.findAll());

        return "admin/artists/manage_artists";
    }

    @GetMapping("/artists/edit_artist/{artistID}")
    public String editArtist(@PathVariable Long artistID, Model model) {
        Artist artist = artistService.findById(artistID);

        model.addAttribute("artist", artist);

        return "admin/artists/create_artist";
    }
    
}
