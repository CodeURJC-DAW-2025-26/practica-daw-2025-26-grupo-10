package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
    private byte[] blobToBytes(Blob blob) {
        if (blob == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image data");
        }
    }

    private byte[] loadResource(String path) {
        try {
            Resource resource = new ClassPathResource(path);
            return StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Default image not found");
        }
    }
    /**
     * Returns a 404 Not Found response for missing images.
     */
    private ResponseEntity<byte[]> buildResponse(byte[] bytes, MediaType mediaType) {
        return ResponseEntity.ok().contentType(mediaType).body(bytes);
    }

    /**
     * Loads the default fallback avatar from the classpath.
     */
    private ResponseEntity<byte[]> buildResponse(Blob blob, MediaType mediaType) {
        return buildResponse(blobToBytes(blob), mediaType);
    }

    // ======================
    // BUSINESS LOGIC METHODS
    // ======================

    /**
     * Retrieves the profile image for a client, or a default avatar if none exists.
     */
    public ResponseEntity<byte[]> getClientImageResponse(Client client) {
        if (client != null && client.getProfileImage() != null && client.getProfileImage().getImageCode() != null) {
            return buildResponse(client.getProfileImage().getImageCode(), MediaType.IMAGE_JPEG);
        }
        // Default avatar PNG
        return buildResponse(loadResource("static/images/default-avatar.png"), MediaType.IMAGE_PNG);
    }
    /**
     * Retrieves the profile image for an artist, or a 404 if not found.
     */
    public ResponseEntity<byte[]> getArtistImageResponse(Artist artist) {
        if (artist != null && artist.getArtistImage() != null && artist.getArtistImage().getImageCode() != null) {
            return buildResponse(artist.getArtistImage().getImageCode(), MediaType.IMAGE_JPEG);
        }
        return ResponseEntity.notFound().build();
    }
    /**
     * Searches for a specific image within an event's gallery.
     */
    public ResponseEntity<byte[]> getEventImageResponse(Event event, Long imageID) {
        if (event != null && event.getEventImages() != null) {
            for (Image image : event.getEventImages()) {
                if (image.getImageID().equals(imageID) && image.getImageCode() != null) {
                    return buildResponse(image.getImageCode(), MediaType.IMAGE_JPEG);
                }
            }
        }
        return ResponseEntity.notFound().build();
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

    public List<Image> createImagesFromFiles(MultipartFile[] files) {
        List<Image> images = new ArrayList<>();

        if (files == null) return images;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    images.add(new Image(
                        file.getOriginalFilename(),
                        new SerialBlob(file.getBytes())
                    ));
                } catch (SQLException | IOException e) {
                    throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error processing image"
                    );
                }
            }
        }

        return images;
    }
}