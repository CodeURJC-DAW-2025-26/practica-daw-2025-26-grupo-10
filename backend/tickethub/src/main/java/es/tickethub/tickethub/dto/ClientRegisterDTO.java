package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientRegisterDTO(
@NotBlank(message = "El nombre es obligatorio")
    String name,

    @NotBlank(message = "El apellido es obligatorio")
    String surname,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    String email,

    @NotBlank(message = "El nombre de usuario es obligatorio")
    String username,

    @NotBlank(message = "La contraseña es obligatoria")
    String password,
    
    @NotBlank(message = "Debes confirmar la contraseña")
    String passwordConfirmation
) {}
