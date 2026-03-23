package es.tickethub.tickethub.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Artist;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.repositories.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ImageService {


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ClientService clientService;

    public byte[] loadExternalImage(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    public byte[] loadImage(String imagePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/images/" + imagePath);
        if (inputStream == null) {
            System.err.println("No se encontró la imagen: " + imagePath);
            return new byte[0];
        }
        return inputStream.readAllBytes();
    }

    public Blob convertToBlob(byte[] imageBytes) throws SQLException {
        return new SerialBlob(imageBytes);
    }

    /**
     * Converts a database Blob into a byte array.
     */
    private byte[] blobToBytes(Blob blob) {
        if (blob == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
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

    public List<Image> createImagesFromFiles(MultipartFile[] files) {
        List<Image> images = new ArrayList<>();

        if (files == null)
            return images;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    images.add(new Image(
                            file.getOriginalFilename(),
                            convertToBlob(file.getBytes())));
                } catch (SQLException | IOException e) {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error processing image");
                }
            }
        }
        return images;
    }

    public Image editClientImage(Client client, MultipartFile newImage, boolean option)
            throws IOException, SQLException {
        byte[] bytes = loadExternalImage(newImage);
        Blob blob = convertToBlob(bytes);

        Image oldImage = client.getProfileImage();


        Image newImgEntity = new Image(client.getName() + "_image", blob);
        newImgEntity = imageRepository.save(newImgEntity);

        client.setProfileImage(newImgEntity);
        clientService.saveClient(client);

        if (oldImage != null && oldImage.getImageID() != 1L) {
            imageRepository.delete(oldImage);
        }

        return newImgEntity;
    }

    public Image editArtistImage(Artist artist, MultipartFile newImage, boolean option)
            throws IOException, SQLException {
        byte[] bytes = loadExternalImage(newImage);
        Blob i = convertToBlob(bytes);

        Image image = artist.getArtistImage();
        if (option || image == null) {
            image = new Image(artist.getArtistName() + "_image", i);
        } else {
            image.setImageCode(i);
        }
        artist.setArtistImage(image);
        artistService.save(artist);
        return image;
    }

    public void deleteArtistImage(Artist artist) {
        Long artistImageID = artist.getArtistImage().getImageID();
        artist.setArtistImage(null);
        imageRepository.deleteById(artistImageID);
    }

    @Transactional
    public void deleteClientImage(Client client) {
        if (client.getProfileImage() == null) return;
        Long oldImageID = client.getProfileImage().getImageID();

        if (oldImageID.equals(1L)) {
            return;
        }

        Image defaultImage = imageRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen default no encontrada"));

        client.setProfileImage(defaultImage);
        clientService.saveClient(client);

        if (!oldImageID.equals(1L)) {
            imageRepository.deleteById(oldImageID);
        }
    }

    public void addImagesToEvent(Event event, MultipartFile[] files) {
        if (files == null || files.length == 0)
            return;
        event.getEventImages().addAll(createImagesFromFiles(files));
    }

    @Transactional
    public void deleteImageFromEvent(Event event, Long imageId) {
        Image imageToDelete = event.getEventImages().stream()
                .filter(img -> img.getImageID().equals(imageId))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada en este evento"));

        event.getEventImages().remove(imageToDelete);
        imageRepository.delete(imageToDelete);
    }
}