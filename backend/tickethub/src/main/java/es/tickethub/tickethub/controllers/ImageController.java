package es.tickethub.tickethub.controllers;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.services.ClientService;
@Controller
@RequestMapping("/images/entities")
public class ImageController {

    @Autowired
    private ClientService clientService;


    @GetMapping("/users/{userID}")
    public ResponseEntity<byte[]> getMethodNgetClientImageame(@PathVariable Long userID) {
        Client client = clientService.getClientById(userID);
        try {
            byte[] imageBytes = client.getProfileImage().getImageCode().getBytes(1,
                    (int) client.getProfileImage().getImageCode().length());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}