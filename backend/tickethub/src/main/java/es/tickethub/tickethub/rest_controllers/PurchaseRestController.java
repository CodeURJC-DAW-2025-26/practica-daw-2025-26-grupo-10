package es.tickethub.tickethub.rest_controllers;

import java.net.URI;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.PurchaseCreateDTO;
import es.tickethub.tickethub.dto.PurchaseDTO;
import es.tickethub.tickethub.dto.PurchaseHistoryDTO;
import es.tickethub.tickethub.dto.PurchaseSliceDTO;
import es.tickethub.tickethub.dto.SessionHistoryDTO;
import es.tickethub.tickethub.dto.TicketBasicDTO;
import es.tickethub.tickethub.dto.TicketSelectionDTO;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.mappers.PurchaseMapper;
import es.tickethub.tickethub.mappers.TicketMapper;
import es.tickethub.tickethub.services.PurchaseService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/public/purchases")
public class PurchaseRestController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private TicketMapper ticketMapper;

    /**
     * Processes a new purchase request from external clients.
     */
    @PostMapping("/save")
    public ResponseEntity<PurchaseDTO> savePurchase(@Valid @RequestBody PurchaseCreateDTO dto) {
        Long sessionId = dto.sessionID();
        String email = dto.name();

        Map<Long, Integer> selections = dto.selections().stream()
                .collect(Collectors.toMap(TicketSelectionDTO::zoneID, TicketSelectionDTO::quantity));

        Purchase saved = purchaseService.processPurchase(sessionId, selections, email);

        URI location = fromCurrentRequest().path("/{purchaseID}").buildAndExpand(saved.getPurchaseID()).toUri();

        return ResponseEntity.created(location).body(purchaseMapper.toDTO(saved));
    }

    @GetMapping("/me")
    public PurchaseSliceDTO getPurchasesByClient(
            @RequestParam(defaultValue = "0") int page,
            Principal principal) {

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes estar autenticado");
        }

        String loggedEmail = principal.getName();
        Slice<Purchase> purchasesSlice = purchaseService.getPurchasesByClientEmail(loggedEmail, page);

        List<PurchaseHistoryDTO> purchases = purchasesSlice.getContent().stream().map(p -> {
            SessionHistoryDTO sessionDTO = new SessionHistoryDTO(
                    p.getSession().getSessionID(),
                    p.getSession().getDate(),
                    p.getSession().getEvent().getName());

            List<TicketBasicDTO> ticketsDTO = ticketMapper.toBasicDTOs(p.getTickets());

            return new PurchaseHistoryDTO(p.getPurchaseID(), ticketsDTO, sessionDTO, p.getTotalPrice());
        }).toList();

        return new PurchaseSliceDTO(purchases, purchasesSlice.hasNext());
    }

    /**
     * Streams the PDF ticket file for a specific purchase.
     * Validates ownership before generation.
     */
    @GetMapping("/download/{purchaseId}")
    public ResponseEntity<byte[]> downloadTickets(@PathVariable Long purchaseId, Principal principal) throws Exception {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        byte[] pdfBytes = purchaseService.generateTicketsPdf(purchaseId, principal.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TicketHub_Order_" + purchaseId + ".pdf")
                .body(pdfBytes);
    }
}