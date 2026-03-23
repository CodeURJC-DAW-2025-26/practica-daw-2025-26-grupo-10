package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.ZoneBasicDTO;
import es.tickethub.tickethub.dto.ZoneCreateDTO;
import es.tickethub.tickethub.dto.ZoneDTO;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.mappers.ZoneMapper;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ZoneService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1")
public class ZoneRestController {
    
    @Autowired private EventService eventService;
    @Autowired private ZoneService zoneService;
    @Autowired private ZoneMapper zoneMapper;

    @GetMapping("/public/events/{eventID}/zones")
    public ResponseEntity<List<ZoneBasicDTO>> getZones (@PathVariable Long eventID) {
        Event event = eventService.findByIdOrThrow(eventID);
        List<Zone> eventZones = event.getZones();
        List<ZoneBasicDTO> zonesDTO= zoneMapper.toBasicDTOs(eventZones);

        return ResponseEntity.ok(zonesDTO);
    }

    @GetMapping("/public/events/{eventID}/zones/{id}")
    public ResponseEntity<ZoneBasicDTO> getZone (@PathVariable Long eventID, @PathVariable Long id) {
        Zone zone = zoneService.findByEventAndID(eventID, id);
        return ResponseEntity.ok(zoneMapper.toBasicDTO(zone));
    }
    

    @PostMapping("/admin/events/{eventID}/zones")
    public ResponseEntity <ZoneDTO> createZone(@Valid @RequestBody ZoneCreateDTO zoneCreateDTO, @PathVariable Long eventID) {
        Zone newZone = zoneMapper.toDomain(zoneCreateDTO);
        zoneService.createAndAssignEvent(newZone, eventID);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newZone.getId()).toUri();
        return ResponseEntity.created(location).body(zoneMapper.toDTO(newZone));
    }
    
    @PutMapping("/admin/events/{eventID}/zones/{id}")
    public ZoneDTO updateZone (@PathVariable Long eventID, @PathVariable Long id, @Valid @RequestBody ZoneBasicDTO zoneBasicDTO) {
        Zone existingZone = zoneService.findByEventAndID(eventID, id);
        zoneMapper.updateEntityFromBasicDTO(zoneBasicDTO, existingZone);
        Zone updated = zoneService.updateZone(eventID, id, existingZone);
        return zoneMapper.toDTO(updated);
    }

    @DeleteMapping("/admin/events/{eventID}/zones/{id}")
    public ResponseEntity<Void> deleteZone (@PathVariable Long eventID, @PathVariable Long id){
        zoneService.deleteZoneFromEvent(eventID, id);
        return ResponseEntity.noContent().build();
    }
}
