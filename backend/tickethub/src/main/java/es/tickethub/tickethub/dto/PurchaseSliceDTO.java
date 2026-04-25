package es.tickethub.tickethub.dto;

import java.util.List;

public record PurchaseSliceDTO(
        List<PurchaseHistoryDTO> content,
        boolean hasNext) {
}
