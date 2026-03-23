package es.tickethub.tickethub.security.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UnauthorizedHandlerJwt implements AuthenticationEntryPoint {
    
  private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandlerJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
      
    logger.info("Unauthorized error: {}", authException.getMessage());

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    String jsonResponse = String.format(
        "{\n" +
        "  \"status\": \"FAILURE\",\n" +
        "  \"message\": \"Acceso denegado\",\n" +
        "  \"data\": \"%s. Path: %s\"\n" +
        "}", 
        authException.getMessage(), request.getServletPath()
    );
    response.getWriter().write(jsonResponse);
  }
}