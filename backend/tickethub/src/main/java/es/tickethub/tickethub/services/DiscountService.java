package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.entities.Event;
import es.tickethub.tickethub.repositories.DiscountRepository;
import es.tickethub.tickethub.repositories.EventRepository;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    EventRepository eventRepository;

    public Discount findById(Long discountID) {
        Optional<Discount> discountOptional = discountRepository.findById(discountID);
        if (!discountOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado");
        }
        return discountOptional.get();
    }

    /* Get all discounts that exist in the DB */
    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        if (discounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay descuentos registrados");
        }
        return discounts;
    }

    public List<Discount> getDiscountsByEvent(Long eventID) {
        List<Discount> discounts = eventRepository.findById(eventID).get().getDiscounts();
        if (discounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay descuentos registrados para este evento");
        }
        return discounts;
    }

    /*
     * Use of existsByDiscountName -> no need to bring the discount. We want to
     * check if it already exists
     */

    public Discount createAndEditDiscount(Discount discount) {

        if (discount.getDiscountID() == null) {
            return discountRepository.save(discount);
        } else {
            Optional<Discount> existing = discountRepository.findById(discount.getDiscountID());

            existing.get().setAmount(discount.getAmount());
            existing.get().setDiscountName(discount.getDiscountName());
            existing.get().setPercentage(discount.getPercentage());
            return discountRepository.save(existing.get());
        }
    }

    public void deleteDiscount(Long discountID) {
        Optional<Discount> optionalDiscount = discountRepository.findById(discountID);
        if (!optionalDiscount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado");
        }
        Discount discount = optionalDiscount.get();
        for (Event event : discount.getEvents()) {
            event.getDiscounts().remove(discount);
            eventRepository.save(event);
        }
        discountRepository.deleteById(discount.getDiscountID());
    }

    /* Apply discount differently whether it's a percentage or not */
    public BigDecimal applyDiscount(BigDecimal originalTicketPrice, Long discountID) {
        Discount discount = findById(discountID);

        if (discount.getPercentage()) {
            BigDecimal discountAmmount = discount.getAmount().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal totalDiscounted = originalTicketPrice.multiply(discountAmmount);
            BigDecimal finalPricePercentage = originalTicketPrice.subtract(totalDiscounted).setScale(2,
                    RoundingMode.HALF_UP);
            return finalPricePercentage;
        } else {
            BigDecimal discountAmmount = discount.getAmount();
            BigDecimal finalPrice = originalTicketPrice.subtract(discountAmmount);
            if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
                return BigDecimal.ZERO;
            }
            return finalPrice.setScale(2, RoundingMode.HALF_UP);
            // return finalPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO :
            // finalPrice.setScale(2, RoundingMode.HALF_UP);
        }
    }
}
