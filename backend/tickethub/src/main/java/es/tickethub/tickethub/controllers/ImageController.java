package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ImageService;

@Controller
@RequestMapping("/images/entities")
public class ImageController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/users/{userID}")
    public ResponseEntity<byte[]> getClientImage(@PathVariable Long userID) {
        Client client = clientService.getClientById(userID);
        return imageService.getClientImageResponse(client);
    }
    
    @GetMapping("/events/{eventID}/image/{imageID}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long eventID, @PathVariable Long imageID) {
        Event event = eventService.findByIdOrThrow(eventID);
        return imageService.getEventImageResponse(event, imageID);
    }

    @GetMapping("/artists/{artistID}")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable Long artistID) {
        Artist artist = artistService.findById(artistID);
        return imageService.getArtistImageResponse(artist);
    }
}