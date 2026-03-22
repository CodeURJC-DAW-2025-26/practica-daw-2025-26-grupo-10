package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.entities.Session;
import es.tickethub.tickethub.entities.Ticket;
import es.tickethub.tickethub.entities.Zone;
import es.tickethub.tickethub.repositories.ClientRepository;
import es.tickethub.tickethub.repositories.PurchaseRepository;

@Service
public class PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ZoneService zoneService;

    @Autowired
    SessionService sessionService;
    /**
     * Processes a complete purchase workflow:
     * 1. Resolves client (new or existing).
     * 2. Calculates and validates total price.
     * 3. Configures session and links tickets.
     * 4. Persists the entire tree.
     */
    @Transactional
    public Purchase processPurchase(Long sessionId, Map<Long, Integer> selections, String email) {
        Client client = clientService.findByEmail(email)
                .orElseGet(() -> clientService.saveClient(new Client(email, "", "", "", "", 0, 0, null, null)));

        Purchase purchase = new Purchase();
        purchase.setClient(client);

        Session session = sessionService.findById(sessionId);
        purchase.setSession(session);

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : selections.entrySet()) {
            Long zoneId = entry.getKey();
            Integer quantity = entry.getValue();
            
            Zone zone = zoneService.findById(zoneId);

            for (int i = 0; i < quantity; i++) {
                Ticket ticket = new Ticket();
                ticket.setZone(zone);
                ticket.setTicketPrice(zone.getPrice());
                ticket.setPurchase(purchase);
                ticket.setIsActive(true);
                ticket.setCode(UUID.randomUUID().toString());
                
                purchase.getTickets().add(ticket);
                calculatedTotal = calculatedTotal.add(zone.getPrice());
            }
        }

        purchase.setTotalPrice(calculatedTotal);
        return purchaseRepository.save(purchase);
    }
    
    /**
     * Retrieves all purchases not matter the client (for admin usage)
     */
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    /**
     * Retrieves all purchases belonging to a specific client
     * Useful for displaying the client's purchase history
     */
    @Transactional(readOnly = true)
    public Slice<Purchase> getPurchasesByClientEmail(String loggedEmail, int pageNumber) {
        if (!clientRepository.existsByEmail(loggedEmail)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "session.date"));
        return purchaseRepository.findByClient_Email(loggedEmail, pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByPurchase(Long purchaseID, String loggedEmail) {
        Purchase purchase = purchaseRepository.findById(purchaseID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        if (!purchase.getClient().getEmail().equals(loggedEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver esta información");
        }
        return purchase.getTickets();
    }

    /**
     * Retrieves a specific purchase by its ID and ensures it belongs to the given
     * client
     * Throws a NOT_FOUND exception if the purchase does not exist or does not
     * belong to the client
     */
    public Purchase getPurchaseByIdAndClient(Long purchaseId, Client client) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findByPurchaseIDAndClient(purchaseId, client);
        if (!optionalPurchase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        return optionalPurchase.get();
    }

    /**
     * Deletes a purchase by its ID
     * If the purchase does not exist, throws a NOT_FOUND exception
     * All associated tickets are automatically deleted due to cascade settings
     */
    @Transactional
    public void deletePurchase(Long purchaseId) {
        if (!purchaseRepository.existsById(purchaseId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        purchaseRepository.deleteById(purchaseId);
    }

    public Purchase getPurchaseById(Long purchaseId) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchaseId);
        if (!optionalPurchase.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        return optionalPurchase.get();
    }

    /**
     * Generates a PDF containing ticket details and QR codes.
     * Includes security validation to prevent unauthorized PDF downloads.
     */
    public byte[] generateTicketsPdf(Long purchaseId, String userEmail) throws Exception {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));

        if (!purchase.getClient().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        return pdfGeneratorService.generatePurchasePdf(purchase);
    }
}
