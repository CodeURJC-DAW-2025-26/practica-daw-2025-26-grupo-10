package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistCreateUpdateDTO(
    @NotBlank(message = "El nombre del artista es obligatorio") 
    String artistName,

    String info,
    String instagram,
    String twitter
) {}
