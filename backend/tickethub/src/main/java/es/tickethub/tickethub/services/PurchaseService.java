package es.tickethub.tickethub.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import es.tickethub.tickethub.entities.Purchase;
import es.tickethub.tickethub.repositories.ClientRepository;
import es.tickethub.tickethub.repositories.PurchaseRepository;

@Service
public class PurchaseService {

    @Autowired PurchaseRepository purchaseRepository;
    @Autowired ClientRepository clientRepository;

    /**
     * Creates a new purchase along with all associated tickets
     * Uses @Transactional to ensure that the purchase and its tickets
     * are saved as a single atomic operation
     */
    @Transactional
    public Purchase createPurchase(Purchase purchase) {
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
    public Slice<Purchase> getPurchasesByClientId(Long clientId,int pageNumber) {
        if(!clientRepository.existsById(clientId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        PageRequest pageRequest =  PageRequest.of(pageNumber,10);
        return purchaseRepository.findByClient_UserID(clientId,pageRequest);
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
}
