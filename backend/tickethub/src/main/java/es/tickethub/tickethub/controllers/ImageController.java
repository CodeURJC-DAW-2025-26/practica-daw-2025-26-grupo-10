package es.tickethub.tickethub.controllers;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.services.ArtistService;
import es.tickethub.tickethub.services.ClientService;
import es.tickethub.tickethub.services.EventService;

@Controller

@RequestMapping("/images/entities")
public class ImageController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ArtistService artistService;

    @GetMapping("/users/{userID}")
    public ResponseEntity<byte[]> getClientImage(@PathVariable Long userID) {
        Client client = clientService.getClientById(userID);

        // Client exists and has photo
        if (client != null && client.getProfileImage() != null) {
            try {
                byte[] imageBytes = client.getProfileImage().getImageCode().getBytes(1,
                        (int) client.getProfileImage().getImageCode().length());

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageBytes);
            } catch (SQLException e) {

            }
        }

        // Client doesn't exist or doesn't have a photo
        try {
            /**
             * Resource: A Spring object used to locate and access files (like images)
             * within the project's 'resources' folder, regardless of the operating system
             */
            Resource defaultImage = new ClassPathResource("static/images/default-avatar.png");
            /**
             * defaultBytes: The actual image content converted into binary data when it's
             * not Blob
             */
            byte[] defaultBytes = StreamUtils.copyToByteArray(defaultImage.getInputStream());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(defaultBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/events/{eventID}/image/{imageID}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long eventID, @PathVariable Long imageID) {
        Event event = eventService.findById(eventID);

        if (event != null && event.getEventImages() != null) {
        try {
            for (Image image : event.getEventImages()) {
                if (image.getImageID().equals(imageID) && image.getImageCode() != null) {

                    byte[] imageBytes = image.getImageCode().getBytes(1, (int) image.getImageCode().length());
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(imageBytes);
                }
            }
        } catch (SQLException e) {

            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Placeholder response
    }
    
    @GetMapping("/artists/{artistID}")
    public ResponseEntity<byte[]> getArtistImage(@PathVariable Long artistID) {
        Artist artist = artistService.findById(artistID);

        if (artist != null && artist.getArtistImage() != null) {
            try {
                byte[] imageBytes = artist.getArtistImage().getImageCode().getBytes(1,
                        (int) artist.getArtistImage().getImageCode().length());

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageBytes);
            } catch (SQLException e) {

            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Placeholder response
    }
}