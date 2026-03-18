package es.tickethub.tickethub.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret = Jwts.SIG.HS256.key().build(); //esto es como una llave criptográfica (una maquina de sellos)
	private final JwtParser jwtParser = Jwts.parser().verifyWith(jwtSecret).build();

	public String tokenStringFromHeaders(HttpServletRequest req){
		String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (bearerToken == null) {
			throw new IllegalArgumentException("Missing Authorization header");
		}
		if(!bearerToken.startsWith("Bearer ")){
			throw new IllegalArgumentException("Authorization header does not start with Bearer: " + bearerToken);
		}
		return bearerToken.substring(7);
	}

	private String tokenStringFromCookies(HttpServletRequest request) {
		var cookies = request.getCookies();
		if (cookies == null) {
			throw new IllegalArgumentException("No cookies found in request");
		}

		for (Cookie cookie : cookies) {
			if (TokenType.ACCESS.tokenName.equals(cookie.getName())) {
				String accessToken = cookie.getValue();
				if (accessToken == null) {
					throw new IllegalArgumentException("Cookie %s has null value".formatted(TokenType.ACCESS.tokenName));
				}

				return accessToken;
			}
		}
		throw new IllegalArgumentException("No access token cookie found in request");
	}

	//estos validate abren el token y me devuelven los datos que habían dentro
	public Claims validateToken(HttpServletRequest req, boolean fromCookie){
		var token = fromCookie?
				tokenStringFromCookies(req):
				tokenStringFromHeaders(req);
		return validateToken(token);
	}

	public Claims validateToken(String token) {
		return jwtParser.parseSignedClaims(token).getPayload();
	}

	//genera el token con duracion corta
	public String generateAccessToken(UserDetails userDetails) {
		return buildToken(TokenType.ACCESS, userDetails).compact();
	}
	
	//genera el token de refresco que tiene la duracion larga
	public String generateRefreshToken(UserDetails userDetails) {
		var token = buildToken(TokenType.REFRESH, userDetails);
        return token.compact();
	}

	//cuado el usuario acierta la contraseña se llama a este método
	private JwtBuilder buildToken(TokenType tokenType, UserDetails userDetails) {
		var currentDate = new Date();
		var expiryDate = Date.from(new Date().toInstant().plus(tokenType.duration));
		return Jwts.builder()
				//los claims son la información que va a viajar dentro de los tokens
				.claim("roles", userDetails.getAuthorities())
				.claim("type", tokenType.name())
				.subject(userDetails.getUsername())//le metes el nombre de usuario
				.issuedAt(currentDate)
				.expiration((Date) expiryDate)//establece la fecha de caducidad del token
				.signWith(jwtSecret);//esto le pone el sello al token
	}
}
