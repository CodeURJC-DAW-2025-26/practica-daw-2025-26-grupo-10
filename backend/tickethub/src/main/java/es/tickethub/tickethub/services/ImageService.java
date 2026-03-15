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
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.mappers.ImageMapper;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public byte[] blobToBytes(Blob blob) {
        if (blob == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada");
        }

        try {
            return blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer la imagen");
        }
    }

    public ResponseEntity<byte[]> buildJpegResponse(Blob blob) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(blobToBytes(blob));
    }

    public ResponseEntity<byte[]> buildPngResponse(byte[] bytes) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bytes);
    }

    public ResponseEntity<byte[]> buildNotFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public byte[] loadDefaultAvatar() {
        try {
            Resource defaultImage = new ClassPathResource("static/images/default-avatar.png");
            return StreamUtils.copyToByteArray(defaultImage.getInputStream());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen por defecto no encontrada");
        }
    }

    // ======================
    // REST METHODS
    // ======================

    

    public ImageDTO toDTO(Image image) {
        return imageMapper.toDTO(image);
    }

    public List<ImageDTO> toDTOs(List<Image> images) {
        return imageMapper.toDTOs(images);
    }

}