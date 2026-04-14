package es.tickethub.tickethub.rest_controllers;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.dto.ClientUpdateDTO;
import es.tickethub.tickethub.dto.PasswordDTO;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.mappers.ClientMapper;
import es.tickethub.tickethub.security.jwt.JwtTokenProvider;
import es.tickethub.tickethub.security.jwt.TokenType;
import es.tickethub.tickethub.security.jwt.UserLoginService;
import es.tickethub.tickethub.services.ClientService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientRestController {
@Autowired
private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserLoginService userLoginService;

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

    //TODO: lo hago esta tarde, esto por ahora es un mierdón. Quien borre esto es francés
    @PutMapping("/me")
    public ClientDTO updateLoggedClient(@Valid @RequestBody ClientUpdateDTO clientUpdateDTO,
            Principal principal, HttpServletResponse response) throws IOException { 
        
        Client client = clientService.getClientByEmail(principal.getName());
        clientMapper.updateEntityFromDto(clientUpdateDTO, client);

        Client updatedClient = clientService.updateClient(client.getEmail(), client, null);

        if (!principal.getName().equals(updatedClient.getEmail())) {
            UserDetails newPrincipal = userDetailsService.loadUserByUsername(updatedClient.getEmail());
            
            String newAccessToken = jwtTokenProvider.generateAccessToken(newPrincipal);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(newPrincipal);
            
            response.addCookie(userLoginService.buildTokenCookie(TokenType.ACCESS, newAccessToken));
            response.addCookie(userLoginService.buildTokenCookie(TokenType.REFRESH, newRefreshToken));
        }

        return clientMapper.toDTO(updatedClient);
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(
            @RequestBody PasswordDTO password,
            Principal principal) {

        String oldPassword = password.oldPassword();
        String newPassword = password.newPassword();
        String newPasswordConfirmation = password.confirmationPassword();

        clientService.changePassword(principal.getName(), oldPassword, newPassword, newPasswordConfirmation);

        return ResponseEntity.ok().build();
    }
}