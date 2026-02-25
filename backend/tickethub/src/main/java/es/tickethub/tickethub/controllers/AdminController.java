package es.tickethub.tickethub.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.repositories.PurchaseRepository;
import es.tickethub.tickethub.services.EventService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping("/admin")
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
    
}
