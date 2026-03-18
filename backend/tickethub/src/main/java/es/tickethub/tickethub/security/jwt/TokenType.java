package es.tickethub.tickethub.security.jwt;

import java.time.Duration;

public enum TokenType {
    //Esto lo que maneja es el tiempo de vida de los tokens
    ACCESS(Duration.ofMinutes(10), "AuthToken"),
    REFRESH(Duration.ofDays(7), "RefreshToken");

    public final Duration duration;
    public final String tokenName;

    TokenType(Duration duration, String tokenName) {
        this.duration = duration;
        this.tokenName = tokenName;
    }
}
