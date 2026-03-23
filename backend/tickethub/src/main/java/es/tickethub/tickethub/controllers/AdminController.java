package es.tickethub.tickethub.controllers;


import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import es.tickethub.tickethub.services.AdminService;


@Controller
@RequestMapping("/admin")
public class AdminController {
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
        model.addAttribute("monthEventData", adminService.getMonthEventData());

        model.addAttribute("rankingLabels", adminService.getRankingLabels());
        model.addAttribute("rankingValues", adminService.getRankingValues());

        model.addAttribute("evolutionLabels", adminService.getEvolutionLabels());
        model.addAttribute("evolutionValues", adminService.getEvolutionValues());

        return "admin/statistics";
    }
}