package es.tickethub.tickethub.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.repositories.PurchaseRepository;
import es.tickethub.tickethub.services.AdminService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("activeEvents", adminService.eventsActive());
        model.addAttribute("numberTickets", adminService.getNumberTickets());
        model.addAttribute("numberUsers", adminService.getNumberUsers());
        model.addAttribute("numberAdmins", adminService.getNumberAdmins());

        return "admin/admin";
    }

    @GetMapping("/statistics")
    public String getStatistics(Model model) {
        // Graph 1: Matrix of tickets sold by month and event
        model.addAttribute("monthEventData", purchaseRepository.getTicketsByMonthAndEvent());

        // Graph 2: Event ranking
        List<Object[]> ranking = purchaseRepository.getRankingByEvent();

        // .map(d -> d[0]) → transforms each element to the event name
        // .collect(Collectors.toList()) → gathers the results into a List
        model.addAttribute("rankingLabels", ranking.stream().map(d -> d[0]).collect(Collectors.toList()));
        model.addAttribute("rankingValues", ranking.stream().map(d -> d[1]).collect(Collectors.toList()));

        // Graph 3: Total tickets evolution
        List<Object[]> evolution = purchaseRepository.getTotalTicketsEvolution();

        // Same logic as above for evolution data
        model.addAttribute("evolutionLabels", evolution.stream().map(d -> d[0]).collect(Collectors.toList()));
        model.addAttribute("evolutionValues", evolution.stream().map(d -> d[1]).collect(Collectors.toList()));
        return "admin/statistics";
    }

}