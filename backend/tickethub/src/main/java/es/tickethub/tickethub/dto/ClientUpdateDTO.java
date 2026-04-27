package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ClientUpdateDTO(
        @NotBlank(message = "El email no puede estar vacío") @Email(message = "Formato de email inválido") String email,

        @NotBlank(message = "El nombre de usuario es obligatorio") String username,

        @NotBlank(message = "El nombre es obligatorio") String name,

        @NotBlank(message = "El apellido es obligatorio") String surname,

        @NotNull(message = "La edad es obligatoria") @Positive(message = "La edad debe ser un número positivo") Integer age,

        @NotNull(message = "El teléfono es obligatorio") Integer phone,

        Long version,

        ImageBasicDTO profileImage) {
}