package es.tickethub.tickethub.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.repositories.ClientRepository;
import es.tickethub.tickethub.services.ClientRecommendationService;
import es.tickethub.tickethub.services.RecommendationService;
import es.tickethub.tickethub.services.ServerRecommendationService;

@Controller
public class HomeController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ServerRecommendationService serverService;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Sirve la página principal y mete recomendaciones si se pasa clientId
     */
    @GetMapping("/")
    public String index(
            Model model,
            @RequestParam(name="clientId", required=false) Long clientId) {

        // 1. TODO: aquí puedes cargar eventsTop, eventsBottom y artists más adelante
        // model.addAttribute("eventsTop", ...);
        // model.addAttribute("eventsBottom", ...);
        // model.addAttribute("artists", ...);

        // 2. Cargar recomendaciones si hay clientId
        if (clientId != null) {
            Client client = clientRepository.findById(clientId).orElse(null);
            if (client != null) {
                ClientRecommendationService clientService = new ClientRecommendationService(client);
                List<Event> recommended = recommendationService.recommendEvents(
                        clientService, serverService, 5, true
                );
                model.addAttribute("recommendedEvents", recommended);
            }
        }

        // 3. Renderizar index.html
        return "index";
    }
}