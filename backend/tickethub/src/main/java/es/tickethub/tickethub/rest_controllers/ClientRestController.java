package es.tickethub.tickethub.rest_controllers;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.dto.ClientUpdateDTO;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.mappers.ClientMapper;
import es.tickethub.tickethub.services.ClientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientMapper clientMapper;

    @GetMapping("/me")
    public ResponseEntity<ClientDTO> getClientByEmail(Principal principal) {
        Client client = clientService.getClientByEmail(principal.getName());
        ClientDTO clientDTO = clientMapper.toDTO(client);
        return ResponseEntity.ok(clientDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<ClientDTO> updateLoggedClient(@Valid @RequestBody ClientUpdateDTO clientUpdateDTO,
            Principal principal) throws IOException {
        Client client = clientService.getClientByEmail(principal.getName());
        clientMapper.updateEntityFromDto(clientUpdateDTO, client);
        Client updatedClient = clientService.updateClient(client.getEmail(), client, null);
        return ResponseEntity.ok(clientMapper.toDTO(updatedClient));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String newPasswordConfirmation,
            Principal principal) {

        clientService.changePassword(
                principal.getName(),
                oldPassword,
                newPassword,
                newPasswordConfirmation);

        return ResponseEntity.noContent().build();
    }
}