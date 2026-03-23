package es.tickethub.tickethub.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest [username=%s, password=%s]".formatted(email, password);
    }
}
