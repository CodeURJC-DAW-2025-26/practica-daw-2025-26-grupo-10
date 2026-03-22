package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistUpdateDTO(
    @NotNull(message = "El ID del artista es obligatorio para actualizar")
    Long artistID,

    @NotBlank(message = "El nombre del artista es obligatorio")
    String artistName,
    
    String info,
    String instagram,
    String twitter,
    ImageBasicDTO artistImage
) {}