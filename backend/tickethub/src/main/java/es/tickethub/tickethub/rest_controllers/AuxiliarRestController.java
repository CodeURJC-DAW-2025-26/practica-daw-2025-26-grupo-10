package es.tickethub.tickethub.rest_controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.dto.ClientRegisterDTO;
import es.tickethub.tickethub.security.jwt.AuthResponse;
import es.tickethub.tickethub.security.jwt.LoginRequest;
import es.tickethub.tickethub.security.jwt.UserLoginService;
import es.tickethub.tickethub.services.ClientService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")
public class AuxiliarRestController {
    private final UserLoginService userLoginService;
    private final ClientService clientService;

    public AuxiliarRestController(UserLoginService userLoginService,ClientService clientService){
        this.userLoginService = userLoginService;
        this.clientService = clientService;
    }

    @PostMapping("/login")
    public ResponseEntity <AuthResponse> login(
        @RequestBody LoginRequest loginRequest,
        HttpServletResponse response){
            return userLoginService.login(response, loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> singUp(
        @Valid @RequestBody ClientRegisterDTO dto) {
        try {
            clientService.registerClient(dto.name(), dto.email(),dto.surname(),
                dto.password(),dto.passwordConfirmation(), dto.username());
            //JSON de exito
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(AuthResponse.Status.SUCCESS,"¡Cuenta creada!"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new AuthResponse(AuthResponse.Status.FAILURE,e.getReason()));
        }
    }
    
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
        @CookieValue(name = "RefreshToken",required = false) String refreshToken,
        HttpServletResponse response) {
        
        return userLoginService.refresh(response,refreshToken);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        userLoginService.logout(response);
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS,"Sesión cerrada correctamente"));
    }
    

}
