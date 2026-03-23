package es.tickethub.tickethub.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private Status status;
    private String message;
    private String error;

    public enum Status {
        SUCCESS, FAILURE
    }

    public AuthResponse() {
    }

    public AuthResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResponse(Status status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginResponse [status=%s, message=%s, error=%s]".formatted(status, message, error);
    }
}
