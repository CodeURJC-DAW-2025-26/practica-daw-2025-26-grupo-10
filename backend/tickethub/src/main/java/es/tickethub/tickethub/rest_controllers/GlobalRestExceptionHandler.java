package es.tickethub.tickethub.rest_controllers;

import java.util.NoSuchElementException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.security.core.AuthenticationException;

import es.tickethub.tickethub.security.jwt.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice(annotations = org.springframework.web.bind.annotation.RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalRestExceptionHandler {
    /**
     * 404 Not Found
     */
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<AuthResponse> handle404(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/api")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse(AuthResponse.Status.FAILURE, "API endpoint does not exists (404)"));
        } else {
            return null;
        }
    }

    /**
     * DTO validation errors (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, errorMessage));
    }

    /**
     * ResponseStatusException
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AuthResponse> handleRestResponseStatusException(ResponseStatusException ex) {
        AuthResponse errorResponse = new AuthResponse(
                AuthResponse.Status.FAILURE, 
                ex.getReason() != null ? ex.getReason() : "An API error occurred"
        );
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    /**
     * NoSuchElementException
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<AuthResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, ex.getMessage()));
    }

    /**
     * 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponse(AuthResponse.Status.FAILURE, "An unexpected server error occurred."));
    }

    /**
 * Spring Security Authentication Errors (401)
 */
@ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
public ResponseEntity<AuthResponse> handleAuthenticationException(Exception ex) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new AuthResponse(AuthResponse.Status.FAILURE, "Credenciales inválidas: email o contraseña incorrectos"));
}
}