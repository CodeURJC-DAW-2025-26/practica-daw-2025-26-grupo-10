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

    /* THIS IS THE GETTER FOR CHARGE THE INDEX HTML (MUST BE FIXED WHEN THE PREFERENCES IS DONE, THE INFORMATION NOW IS ONLY TO CHARGE THE PAGE) */
    @GetMapping("/index")
    public String showIndex(Model model, Principal principal) {
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
        if (principal.getName() != null) {
            Optional<Client> client = clientRepository.findByEmail(principal.getName());
            if (client.isPresent()) {
                ClientRecommendationService clientService = new ClientRecommendationService(client.get());
                List<Event> recommended = recommendationService.recommendEvents(
                        clientService, serverService, 5, true
                );
                model.addAttribute("recommendedEvents", recommended);
            }
        }

        model.addAttribute("eventsTop", eventsTop);
        model.addAttribute("eventsBottom", eventsBottom);
        model.addAttribute("artists", artists);

        return "public/index";
    }

    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "public/login";
    }

    @GetMapping("/signup")
    public String showSignup() {
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
        model.addAttribute("mensajeError", "No tienes permiso para acceder a este recurso.");
        return "error/403";
    }

    @GetMapping("/error/404")
    public String show404Error() {
        return "error/404";
    }

}
