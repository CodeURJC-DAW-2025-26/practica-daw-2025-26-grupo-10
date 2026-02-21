package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    @Autowired ZoneService zoneService;

    // Show all discounts
    @GetMapping
    public String listZones(Model model) {
        model.addAttribute("discounts", zoneService.findAll());
        return "discounts";
    }

    // New discount form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("zone", new Zone());
        return "create-zone"; 
    }

    // Creation of new discount
    @PostMapping
    public String createZone(@Valid Zone zone, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "create-zone";
        }

        try {
            zoneService.save(zone);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-zone";
        }

        return "redirect:/zones";
    }

    @GetMapping("/{name}")
    public String showZone(@PathVariable Long id, Model model) {
        Zone zone = zoneService.findById(id);
        model.addAttribute("zone", zone);
        return "zone";
    }

    @GetMapping("/delete/{name}")
    public String deleteZone(@PathVariable Long id) {
        zoneService.deleteById(id);
        return "redirect:/zones";
    }
}
