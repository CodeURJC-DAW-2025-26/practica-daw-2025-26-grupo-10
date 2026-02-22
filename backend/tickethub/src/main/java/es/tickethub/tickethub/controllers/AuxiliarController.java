package es.tickethub.tickethub.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.EventService;


@Controller
@RequestMapping("/public")
public class AuxiliarController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ArtistService artistService;

    /* THIS IS THE GETTER FOR CHARGE THE INDEX HTML (MUST BE FIXED WHEN THE PREFERENCES IS DONE, THE INFORMATION NOW IS ONLY TO CHARGE THE PAGE) */
    @GetMapping("index")
    public String showIndex(Model model) {
        List <Event> eventsTop = new ArrayList<>();
        List <Event> eventsBottom = new ArrayList<>();
        List <Map <String, Object> > artists = new ArrayList<>();

        long i;
        for (i = 1; i <= 3; i++) {
            Event event = eventService.findById(i);
            eventsTop.add(event);
            if (i != 3) {
                eventsBottom.add(event);
            }
        }

        for (i = 1; i < 5; i++) {
            Artist artist = artistService.findById(i);
            
            Map<String, Object> artistInfo = new HashMap<>();
            artistInfo.put("artist", artist);
            artistInfo.put("eventsIncoming", artist.getEventsIncoming() != null ? artist.getEventsIncoming().size(): 0);
            
            artists.add(artistInfo);
        }

        model.addAttribute("eventsTop", eventsTop);
        model.addAttribute("eventsBottom", eventsBottom);
        model.addAttribute("artists", artists);

        return "public/index";
    }

    @GetMapping("login")
    public String showLogin() {

        return "public/login";
    }
    
    @GetMapping("signup")
    public String showSignup() {

        return "public/signup";
    }

}
