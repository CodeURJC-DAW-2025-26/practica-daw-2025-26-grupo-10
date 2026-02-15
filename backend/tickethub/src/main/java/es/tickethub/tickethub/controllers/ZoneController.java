package es.tickethub.tickethub.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.services.ZoneService;

@RestController
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping("/zones") //no hay id si lo pillas todo
    public List<Zone> getAllZones() {
        return zoneService.findAll();
    }

    @GetMapping("/zones/{id}")
    public Zone getZoneById(@PathVariable Long id) {
        return zoneService.findById(id);
    }

    @PostMapping("/zones")
    public Zone createZone(@RequestBody Zone zone) {
        return zoneService.save(zone);
    }
}
