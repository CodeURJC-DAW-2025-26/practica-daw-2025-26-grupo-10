package es.tickethub.tickethub.dto;

import java.util.List;

public record ClientDTO(
    Long userID,
    String email,
    String username,
    Boolean admin,
    Long version,
    List<PurchaseBasicDTO> purchases,
    String name,
    String surname,
    Integer age,
    Integer phone,
    ImageBasicDTO profileImage
) {}