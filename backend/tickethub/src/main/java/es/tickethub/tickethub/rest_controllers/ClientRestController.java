package es.tickethub.tickethub.rest_controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.services.ClientService;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/me")
    public ResponseEntity<ClientDTO> getLoggedClient(Principal principal) {
        ClientDTO clientDTO = clientService.getLoggedClientDTO(principal.getName());
        return ResponseEntity.ok(clientDTO);
    }

    @PutMapping("/me")
    public ResponseEntity<ClientDTO> updateLoggedClient(
            @RequestBody ClientDTO clientDTO,
            Principal principal) {

        ClientDTO updatedClient = clientService.updateClientREST(principal.getName(), clientDTO);
        return ResponseEntity.ok(updatedClient);
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
