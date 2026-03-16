package es.tickethub.tickethub.rest_controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ImageService;

@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {

    @Autowired private ClientService clientService;
    @Autowired private EventService eventService;
    @Autowired private ArtistService artistService;
    @Autowired private ImageService imageService;

    @GetMapping("/users/{userID}")
    public ResponseEntity<byte[]> getClientImage(@PathVariable Long userID) {
        return imageService.getClientImageResponse(clientService.getClientById(userID));
    }

    @GetMapping("/artists/{artistID}")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable Long artistID) {
        return imageService.getArtistImageResponse(artistService.findById(artistID));
    }

    @GetMapping("/events/{eventID}/image/{imageID}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long eventID, @PathVariable Long imageID) {
        Optional<Event> event = eventService.findById(eventID);
        return imageService.getEventImageResponse(event.orElse(null), imageID);
    }
}
