package es.tickethub.tickethub.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExeceptionHandler {
    
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, Model model) {
        //
        model.addAttribute("mensajeError", ex.getReason());
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return "error/404";
        } else if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            return "error/403";
        }
        return "error/500"; 
    }
}
