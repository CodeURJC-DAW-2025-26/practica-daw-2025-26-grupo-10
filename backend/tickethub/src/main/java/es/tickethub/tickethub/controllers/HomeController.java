package es.tickethub.tickethub.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventRankingService;
import es.tickethub.tickethub.services.EventRecommendationService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ServerRecommendationService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/public")
public class HomeController {

    @Autowired
    private ServerRecommendationService serverService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRankingService eventRankingService;

    @Autowired
    private EventRecommendationService eventRecommendationService;
    
    @Autowired
    private ClientService clientService;

    @Autowired
    private ArtistService artistService;

    @GetMapping("/selector")
    public String selectTypeOfIndex(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/admin";
        }
        return "redirect:/public/index";
    }

    @GetMapping("/index")
    public String showIndex(Model model, Principal principal) {

        model.addAttribute("eventsTop", eventRankingService.getTopSellingEvents(3));
        model.addAttribute("eventsBottom", eventRankingService.getNextUpcomingEvents(2));

        model.addAttribute("artists", artistService.getPopularArtists(4));

        if (principal != null) {
            clientService.findByEmail(principal.getName())
                .ifPresent(client -> model.addAttribute(
                        "recommendedEvents",
                        eventRecommendationService.getRecommendedEvents(client,  5)
                ));
        }

        return "public/index";
    }
}