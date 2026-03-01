package es.tickethub.tickethub.controllers;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientRecommendationService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.RecommendationService;
import es.tickethub.tickethub.services.ServerRecommendationService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/public")
public class AuxiliarController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ServerRecommendationService serverService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ArtistService artistService;

    @GetMapping("/selector")
    public String selectTypeOfIndex(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/admin";
        } else {
            return "redirect:/public/index";
        }
    }

    /*
     * THIS IS THE GETTER FOR CHARGE THE INDEX HTML.
     * WE DECLARE TWO MAPS, ONE TO ORDER THE ARTISTS BY THEIR NUMBER OF EVENTS AND OTHER TO ORDER THE EVENTS BY THEIR POPULARITY.
     * WE ALSO DECLARE A LIST OF EVENTS ORDERED BY THEIR DATE TO SHOW THE TWO MOST PROXIMITY EVENTS.
     * IF THE USER IS LOGGED, WE ALSO GET A LIST OF RECOMMENDED EVENTS FOR HIM.
     */
    @GetMapping("/index")
    public String showIndex(Model model, Principal principal) {
        Map<Artist , Integer> artistWithMoreEvents = new LinkedHashMap<>();
        Map<Event, Integer> eventPopularity = new LinkedHashMap<>();

        List<Event> events = eventService.findAll();

        List<Event> eventsBottom = new ArrayList<>(events);
        eventsBottom.sort(Comparator.comparing(event ->
            event.getSessions().stream().map(session -> session.getDate().toInstant())
                .filter(date -> !date.isBefore(Instant.now()))
                .min(Instant::compareTo)
                .orElse(Instant.MAX)
        ));

        events.sort((e1, e2) -> Integer.compare(
                e2.getSessions().stream().flatMap(session -> session.getPurchases().stream())
                .mapToInt(purchase -> purchase.getTickets().size()).sum(),
                e1.getSessions().stream().flatMap(session -> session.getPurchases().stream())
                .mapToInt(purchase -> purchase.getTickets().size()).sum()
            ));

        for (Event event : events) {
            if (event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                event.setMainImage(event.getEventImages().get(0));
            }
            
            Integer popularity = event.getSessions().stream().flatMap(session -> session.getPurchases().stream())
                    .mapToInt(purchase -> purchase.getTickets().size()).sum();
            
            eventPopularity.put(event, popularity);
        }

        /* To sort the artists by their number of events*/
        List<Artist> artists = artistService.findAll();
        artists.sort((a1, a2) -> Integer.compare(a2.getEventsIncoming().size() + a2.getLastEvents().size(), a1.getEventsIncoming().size() + a1.getLastEvents().size()));
        
        for (Artist artist : artists) {
            int numEvents = artist.getEventsIncoming().size() + artist.getLastEvents().size();
            artistWithMoreEvents.put(artist, numEvents);
        }
        
        if (principal != null) {
            Optional <Client> client = clientService.getClientRepository().findByEmail(principal.getName());
            if (client.isPresent()) {
                ClientRecommendationService clientService = new ClientRecommendationService(client.get());
                List<Event> recommended = recommendationService.recommendEvents(
                        clientService, serverService, 5, true
                );
                model.addAttribute("recommendedEvents", recommended);
            }
        }

        model.addAttribute("eventsTop", eventPopularity.entrySet().stream().limit(3).toList());
        model.addAttribute("eventsBottom", eventsBottom.stream().limit(2).toList());
        model.addAttribute("artists", artistWithMoreEvents.entrySet().stream().limit(4).toList());

        return "public/index";
    }

    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(required = false) String error,Principal principal) {
        if(principal != null){
            return "redirect:/public/selector";
        }
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "public/login";
    }

    @GetMapping("/signup")
    public String showSignup(Principal principal) {
        if(principal != null){
            return "redirect:/public/selector";
        }
        return "public/signup";
    }

    /*
     * THIS IS THE METHOD TO REGISTER A NEW CLIENT.
     */
    @PostMapping("/registration")
    public String registeClient(@RequestParam String name,@RequestParam String email,@RequestParam String surname,
            @RequestParam String password,@RequestParam String passWordConfirmation,RedirectAttributes redirectAttributes,
            @RequestParam String username) {
        try {
            clientService.registeClient(name, email, surname, password, passWordConfirmation,username);
            redirectAttributes.addFlashAttribute("success", "¡Cuenta creada! Ya puedes iniciar sesión.");
            return "redirect:/public/login";

        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
            return "redirect:/public/signup";
        }
    }

    @GetMapping("/error/403")
    public String show403Error(Model model) {
        model.addAttribute("messageError", "No tienes permiso para acceder a este recurso.");
        return "error/403";
    }

    @GetMapping("/error/404")
    public String show404Error() {
        return "error/404";
    }

}
