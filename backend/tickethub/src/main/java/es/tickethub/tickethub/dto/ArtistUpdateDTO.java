package es.tickethub.tickethub.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistUpdateDTO(
    Long artistID,
    @NotBlank String artistName,
    String info,
    String instagram,
    String twitter,
    ImageBasicDTO artistImage
) {}