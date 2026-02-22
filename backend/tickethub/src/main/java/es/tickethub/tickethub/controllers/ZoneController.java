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
@RequestMapping("admin/zones")
public class ZoneController {

    @Autowired ZoneService zoneService;

    // Show all zones
    @GetMapping("/manage_zones")
    public String listZones(Model model) {

        model.addAttribute("zones", zoneService.findAll());

        return "admin/zones/manage_zones";
    }

    // New discount form
    @GetMapping("/create_zone")
    public String showCreateForm(Model model) {
        model.addAttribute("zone", new Zone());
        return "admin/zones/create_zone"; 
    }

    // Creation of new discount
    @PostMapping
    public String createZone(@Valid Zone zone, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "admin/zones/create_zone";
        }

        try {
            zoneService.save(zone);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/zones/create_zone";
        }

        return "redirect:/admin/admin";
    }

    @GetMapping("/edit_zone/{id}")
    public String showZone(@PathVariable Long id, Model model) {

        Zone zone = zoneService.findById(id);
        model.addAttribute("zone", zone);

        return "admin/zones/create_zone";
    }

    @GetMapping("/delete_zone/{id}")
    public String deleteZone(@PathVariable Long id) {
        zoneService.deleteById(id);
        return "redirect:/admin/zones/manage_zones";
    }
}
