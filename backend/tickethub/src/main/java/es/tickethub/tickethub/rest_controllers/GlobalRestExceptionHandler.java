package es.tickethub.tickethub.rest_controllers; // Asegúrate de que el paquete es correcto

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import es.tickethub.tickethub.security.jwt.AuthResponse;
import org.springframework.security.authentication.BadCredentialsException;

@RestControllerAdvice(basePackages = "es.tickethub.tickethub.rest_controllers")
public class GlobalRestExceptionHandler {

    //  @Valid  DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, errorMessage));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AuthResponse> handleRestResponseStatusException(ResponseStatusException ex) {
        AuthResponse errorResponse = new AuthResponse(
                AuthResponse.Status.FAILURE, 
                "Ha ocurrido un error en la API", 
                ex.getReason()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    //if something at the bbdd doesnt exists
    @ExceptionHandler({jakarta.persistence.EntityNotFoundException.class, java.util.NoSuchElementException.class})
    public ResponseEntity<AuthResponse> handleNotFoundExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, "El recurso solicitado no existe o no fue encontrado"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handleGenericException(Exception ex) {
        // Imprimimos el error real en la consola de VS Code para que tú lo veas
        ex.printStackTrace(); 
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, "Error interno del servidor. Contacte con el administrador."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, "Email o contraseña incorrectos"));
    }
}