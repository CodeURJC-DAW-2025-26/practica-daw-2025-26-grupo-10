package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import es.tickethub.tickethub.entities.Client;
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
     * Retrieves a specific purchase by its ID and ensures it belongs to the given client
     * Throws a NOT_FOUND exception if the purchase does not exist or does not belong to the client
     */
    public Purchase getPurchaseByIdAndClient(Long purchaseId, Client client) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findByIdAndClient(purchaseId, client);
        if (!optionalPurchase.isPresent()){
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
}
