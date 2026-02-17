package es.tickethub.tickethub.controllers;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;


import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ZoneService;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    public String getAllZones(Model model) {
        List<Zone> zones = zoneService.findAll();
        model.addAttribute("zones", zones);
        return "zones"; // zones.html en templates
    }


    @GetMapping("/{id}")
    public String getZoneById(@PathVariable Long id, Model model) {
        Zone zone = zoneService.findById(id);
        model.addAttribute("zone", zone);
        return "zone-detail";
    }

    public String createZone(@RequestBody Zone zone) {
        zoneService.save(zone);
        return "redirect:/zones";
    }
}
