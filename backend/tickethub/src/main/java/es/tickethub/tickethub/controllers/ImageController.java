package es.tickethub.tickethub.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.repositories.ImageRepository;

@Controller
@RequestMapping("public/image/")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;
    
    /* METHOD FOR RENDERING THE IMAGE TO THE HTML*/
    @GetMapping("{name}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String name) {
        try {
            Image image = imageRepository.findById(name)
                    .orElseThrow(() -> new RuntimeException("Image not found: " + name));

            byte[] bytes = image.getImageCode().getBytes(1, (int) image.getImageCode().length());
        
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // turn to .png
                .body(bytes);
        } catch (SQLException e) {
            System.err.println("Error at serving image: " + name + " - " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
