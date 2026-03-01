package es.tickethub.tickethub.controllers;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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


    @GetMapping("/index")
    public String showIndex(Model model, Principal principal) {
        List<Event> allEvents = eventService.findAll();

        allEvents.sort((e1, e2) -> {
            int sales1 = (e1.getSessions() == null) ? 0
                    : e1.getSessions().stream()
                            .flatMap(s -> s.getPurchases().stream())
                            .mapToInt(p -> p.getTickets().size()).sum();
            int sales2 = (e2.getSessions() == null) ? 0
                    : e2.getSessions().stream()
                            .flatMap(s -> s.getPurchases().stream())
                            .mapToInt(p -> p.getTickets().size()).sum();
            return Integer.compare(sales2, sales1);
        });

        // Configurate image
        List<Event> eventsTop = allEvents.stream().limit(3).toList();
        for (Event event : eventsTop) {
            if (event.getEventImages() != null && !event.getEventImages().isEmpty()) {
                event.setMainImage(event.getEventImages().get(0));
            }
        }

        // Next events
        List<Event> eventsBottom = new ArrayList<>(allEvents);
        eventsBottom.sort(Comparator.comparing(event -> {
            if (event.getSessions() == null || event.getSessions().isEmpty())
                return Instant.MAX;
            return event.getSessions().stream()
                    .map(session -> session.getDate().toInstant())
                    .filter(date -> !date.isBefore(Instant.now()))
                    .min(Instant::compareTo)
                    .orElse(Instant.MAX);
        }));

        // Artist
        List<Artist> artists = artistService.findAll();
        artists.sort((a1, a2) -> {
            int count1 = ((a1.getEventsIncoming() != null) ? a1.getEventsIncoming().size() : 0)
                    + ((a1.getLastEvents() != null) ? a1.getLastEvents().size() : 0);
            int count2 = ((a2.getEventsIncoming() != null) ? a2.getEventsIncoming().size() : 0)
                    + ((a2.getLastEvents() != null) ? a2.getLastEvents().size() : 0);
            return Integer.compare(count2, count1);
        });

        // Recommendations
        if (principal != null) {
            try {
                clientService.getClientRepository().findByEmail(principal.getName()).ifPresent(c -> {
                    ClientRecommendationService crs = new ClientRecommendationService(c);
                    List<Event> recommended = recommendationService.recommendEvents(crs, serverService, 5, true);
                    model.addAttribute("recommendedEvents", recommended);
                });
            } catch (Exception e) {
                System.err.println("Error en recomendaciones: " + e.getMessage());
            }
        }

        // Send lists
        model.addAttribute("eventsTop", eventsTop);
        model.addAttribute("eventsBottom", eventsBottom.stream().limit(2).toList());
        model.addAttribute("artists", artists.stream().limit(4).toList());

        return "public/index";
    }

    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(required = false) String error, Principal principal) {
        if (principal != null) {
            return "redirect:/public/selector";
        }
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "public/login";
    }

    @GetMapping("/signup")
    public String showSignup(Principal principal) {
        if (principal != null) {
            return "redirect:/public/selector";
        }
        return "public/signup";
    }

    /*
     * THIS IS THE METHOD TO REGISTER A NEW CLIENT.
     */
    @PostMapping("/registration")
    public String registeClient(@RequestParam String name, @RequestParam String email, @RequestParam String surname,
            @RequestParam String password, @RequestParam String passWordConfirmation,
            RedirectAttributes redirectAttributes,
            @RequestParam String username) {
        try {
            clientService.registeClient(name, email, surname, password, passWordConfirmation, username);
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
