package es.tickethub.tickethub.rest_controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.mappers.ClientMapper;
import es.tickethub.tickethub.services.ClientService;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientMapper clientMapper;

    @GetMapping("/me")
    public ResponseEntity<ClientDTO> getLoggedClient(Principal principal) {
        Client client = clientService.getLoggedClient(principal.getName());
        ClientDTO clientDTO = clientMapper.toDTO(client);
        return ResponseEntity.ok(clientDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<ClientDTO> updateLoggedClient(
            @RequestBody ClientDTO clientDTO,
            Principal principal) {

        Client client = clientMapper.toDomain(clientDTO);
        Client updatedClient = clientService.updateClientREST(principal.getName(), client);

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
                newPasswordConfirmation
        );

        return ResponseEntity.noContent().build();
    }
}