package es.tickethub.tickethub.dto;

import java.sql.Timestamp;

public record SessionHistoryDTO(
        Long sessionID,
        Timestamp date,
        String eventName) {
}
