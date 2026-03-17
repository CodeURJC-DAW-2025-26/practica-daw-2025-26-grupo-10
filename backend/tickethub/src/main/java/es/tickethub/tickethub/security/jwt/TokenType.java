package es.tickethub.tickethub.security.jwt;

import java.time.Duration;

public enum TokenType {

    ACCESS(Duration.ofMinutes(5), "AuthToken"),
    REFRESH(Duration.ofDays(7), "RefreshToken")

    public final Duration duration;
    public final String tokenName;

    TokenType(Duration duration, String tokenName) {
        this.duration = duration;
        this.tokenName = tokenName;
    }
}
