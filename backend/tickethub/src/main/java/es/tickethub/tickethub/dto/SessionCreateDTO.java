package es.tickethub.tickethub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SessionCreateDTO(
    
    @JsonProperty("eventID") Long eventID,
    @JsonProperty("dateStr") String dateStr
) {}
