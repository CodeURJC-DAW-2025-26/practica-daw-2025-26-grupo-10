package es.tickethub.tickethub.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.dto.PurchaseCreateDTO;
import es.tickethub.tickethub.dto.PurchaseDTO;
import es.tickethub.tickethub.dto.TicketSelectionDTO;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.mappers.PurchaseMapper;
import es.tickethub.tickethub.services.PurchaseService;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchases")
public class PurchaseRestController {

    @Autowired private PurchaseService purchaseService;
    @Autowired private PurchaseMapper purchaseMapper;

    /**
     * Processes a new purchase request from external clients.
     */
    @PostMapping("/save")
    public ResponseEntity<PurchaseDTO> savePurchase(@RequestBody PurchaseCreateDTO dto, Principal principal) {
        Long sessionId = dto.sessionID();
        String email = principal.getName();
        
        Map<Long, Integer> selections = dto.selections().stream()
            .collect(Collectors.toMap(TicketSelectionDTO::zoneID, TicketSelectionDTO::quantity));

        Purchase saved = purchaseService.processPurchase(sessionId, selections, email);
        
        return new ResponseEntity<>(purchaseMapper.toDTO(saved), HttpStatus.CREATED);
    }

    /**
     * Streams the PDF ticket file for a specific purchase.
     * Validates ownership before generation.
     */
    @GetMapping("/download/{purchaseId}")
    public ResponseEntity<byte[]> downloadTickets(@PathVariable Long purchaseId, Principal principal) throws Exception {
        byte[] pdfBytes = purchaseService.generateTicketsPdf(purchaseId, principal.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TicketHub_Order_" + purchaseId + ".pdf")
                .body(pdfBytes);
    }
}