package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistCreateDTO(
    @NotBlank(message = "El nombre del artista es obligatorio") 
    String artistName,

    String info,
    String instagram,
    String twitter,

    @NotNull(message = "El artista debe tener una imagen asociada")    
    ImageBasicDTO artistImage
) {}
