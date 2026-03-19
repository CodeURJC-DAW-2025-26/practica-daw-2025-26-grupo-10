package es.tickethub.tickethub.rest_controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.security.jwt.AuthResponse;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    // Captura los errores del @Valid de los DTOs
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

}
