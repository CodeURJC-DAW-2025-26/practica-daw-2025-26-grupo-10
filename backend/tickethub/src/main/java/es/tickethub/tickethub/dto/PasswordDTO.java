package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotEmpty;

public record PasswordDTO(

    @NotEmpty(message = "Debes introducir la contraseña actual")
    String oldPassword,
    
    @NotEmpty(message = "Debes introducir una contraseña nueva")
    String newPassword,
    
    @NotEmpty(message = "Debes confirmar la contraseña nueva")
    String confirmationPassword
) {}
