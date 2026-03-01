package es.tickethub.tickethub.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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
import es.tickethub.tickethub.repositories.ClientRepository;
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
    private ClientRepository clientRepository;

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
     * THIS IS THE GETTER FOR CHARGE THE INDEX HTML (MUST BE FIXED WHEN THE
     * PREFERENCES IS DONE, THE INFORMATION NOW IS ONLY TO CHARGE THE PAGE)
     */
    @GetMapping("/index")
    public String showIndex(Model model, Principal principal) {
        // 1. Carga de eventos de forma segura
        List<Event> allEvents = eventService.findPaginated(0, 3);
        List<Event> eventsTop = new ArrayList<>();
        List<Event> eventsBottom = new ArrayList<>();

        for (Event e : allEvents) {
            if (e != null) { // Seguridad ante todo
                eventsTop.add(e);
                eventsBottom.add(e); // Simplificado para pruebas
            }
        }

        // 2. Carga de artistas (SOLUCIÓN AL ERROR 500)
        List<Map<String, Object>> artists = new ArrayList<>();
        for (long i = 1; i < 5; i++) {
            Artist artist = artistService.findById(i);
            if (artist != null) { // SI EL ARTISTA NO EXISTE, NO LO METEMOS
                Map<String, Object> artistInfo = new HashMap<>();
                artistInfo.put("artist", artist);
                // Comprobamos si tiene eventos para no romper al hacer .size()
                int incoming = (artist.getEventsIncoming() != null) ? artist.getEventsIncoming().size() : 0;
                artistInfo.put("eventsIncoming", incoming);
                artists.add(artistInfo);
            }
        }

        // 3. Recomendaciones (Solo si el Principal existe)
        if (principal != null) {
            clientRepository.findByEmail(principal.getName()).ifPresent(c -> {
                try {
                    ClientRecommendationService crs = new ClientRecommendationService(c);
                    List<Event> recommended = recommendationService.recommendEvents(crs, serverService, 5, true);
                    model.addAttribute("recommendedEvents", recommended);
                } catch (Exception e) {
                    // Si el algoritmo falla, el usuario logueado sigue viendo el index sin romper
                    System.out.println("Error en algoritmo: " + e.getMessage());
                }
            });
        }

        model.addAttribute("eventsTop", eventsTop);
        model.addAttribute("eventsBottom", eventsBottom);
        model.addAttribute("artists", artists);

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
