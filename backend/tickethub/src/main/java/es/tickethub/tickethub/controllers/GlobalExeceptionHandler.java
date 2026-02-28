package es.tickethub.tickethub.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExeceptionHandler {
    
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, Model model,HttpServletResponse response) {
    
        model.addAttribute("messageError", ex.getReason());
        model.addAttribute("status",ex.getStatusCode().value());
        response.setStatus(ex.getStatusCode().value());
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return "error/404";
        } else if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            return "error/403";
        }
        return "error/500";
    }
}
