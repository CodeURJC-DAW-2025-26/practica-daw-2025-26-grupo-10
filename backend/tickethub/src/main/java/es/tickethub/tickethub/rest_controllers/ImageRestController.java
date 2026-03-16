package es.tickethub.tickethub.rest_controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventService;
import es.tickethub.tickethub.services.ImageService;

@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {

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

        if (client.getProfileImage() != null && client.getProfileImage().getImageCode() != null) {
            return imageService.buildJpegResponse(client.getProfileImage().getImageCode());
        }

        return imageService.buildPngResponse(imageService.loadDefaultAvatar());
    }

    @GetMapping("/artists/{artistID}")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable Long artistID) {
        Artist artist = artistService.findById(artistID);

        if (artist.getArtistImage() != null && artist.getArtistImage().getImageCode() != null) {
            return imageService.buildJpegResponse(artist.getArtistImage().getImageCode());
        }

        return imageService.buildNotFoundResponse();
    }

    @GetMapping("/events/{eventID}/image/{imageID}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long eventID, @PathVariable Long imageID) {
        Optional <Event> optionalEvent = eventService.findById(eventID);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            if (event.getEventImages() != null) {
                for (Image image : event.getEventImages()) {
                    if (image.getImageID().equals(imageID) && image.getImageCode() != null) {
                        return imageService.buildJpegResponse(image.getImageCode());
                    }
                }
            }
        }

        return imageService.buildNotFoundResponse();
    }
}
