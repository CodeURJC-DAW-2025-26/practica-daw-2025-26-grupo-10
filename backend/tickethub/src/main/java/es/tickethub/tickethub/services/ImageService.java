package es.tickethub.tickethub.services;

import java.util.List;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.dto.ImageDTO;
import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.mappers.ImageMapper;


@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    /**
     * Converts a database Blob into a byte array.
     */
    public byte[] blobToBytes(Blob blob) {
        if (blob == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }

        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image data");
        }
    }

    /**
     * Builds an HTTP ResponseEntity containing a JPEG image from a Blob.
     */
    public ResponseEntity<byte[]> buildJpegResponse(Blob blob) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(blobToBytes(blob));
    }

    /**
     * Builds an HTTP ResponseEntity containing a PNG image from a byte array.
     */
    public ResponseEntity<byte[]> buildPngResponse(byte[] bytes) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }

    /**
     * Returns a 404 Not Found response for missing images.
     */
    public ResponseEntity<byte[]> buildNotFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Loads the default fallback avatar from the classpath.
     */
    public byte[] loadDefaultAvatar() {
        try {
            Resource defaultImage = new ClassPathResource("static/images/default-avatar.png");
            return StreamUtils.copyToByteArray(defaultImage.getInputStream());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Default avatar image not found");
        }
    }

    // ======================
    // BUSINESS LOGIC METHODS
    // ======================

    /**
     * Retrieves the profile image for a client, or a default avatar if none exists.
     */
    public ResponseEntity<byte[]> getClientImageResponse(Client client) {
        if (client != null && client.getProfileImage() != null && client.getProfileImage().getImageCode() != null) {
            return buildJpegResponse(client.getProfileImage().getImageCode());
        }
        return buildPngResponse(loadDefaultAvatar());
    }

    /**
     * Retrieves the profile image for an artist, or a 404 if not found.
     */
    public ResponseEntity<byte[]> getArtistImageResponse(Artist artist) {
        if (artist != null && artist.getArtistImage() != null && artist.getArtistImage().getImageCode() != null) {
            return buildJpegResponse(artist.getArtistImage().getImageCode());
        }
        return buildNotFoundResponse();
    }

    /**
     * Searches for a specific image within an event's gallery.
     */
    public ResponseEntity<byte[]> getEventImageResponse(Event event, Long imageID) {
        if (event != null && event.getEventImages() != null) {
            for (Image image : event.getEventImages()) {
                if (image.getImageID().equals(imageID) && image.getImageCode() != null) {
                    return buildJpegResponse(image.getImageCode());
                }
            }
        }
        return buildNotFoundResponse();
    }

    // ======================
    // DTO MAPPING METHODS
    // ======================

    public ImageDTO toDTO(Image image) {
        return imageMapper.toDTO(image);
    }

    public List<ImageDTO> toDTOs(List<Image> images) {
        return imageMapper.toDTOs(images);
    }
}