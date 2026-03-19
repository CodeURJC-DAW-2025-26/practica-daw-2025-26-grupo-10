package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Discount> getDiscountsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return discountRepository.findAll(pageable);
    }

    public List<Discount> getDiscountsByEvent(Long eventID) {
        List<Discount> discounts = eventRepository.findById(eventID).get().getDiscounts();
        if (discounts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay descuentos registrados para este evento");
        }
        return discounts;
    }

    public Discount editDiscount(Discount discount, Long id) {
        Discount existing = discountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado"));

        existing.setAmount(discount.getAmount());
        existing.setDiscountName(discount.getDiscountName());
        existing.setPercentage(discount.getPercentage());

        return discountRepository.save(existing);
    }

    public Discount save(Discount discount) {
        if (discountRepository.findByDiscountName(discount.getDiscountName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un descuento con ese nombre.");
        }
        return discountRepository.save(discount);
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
            BigDecimal discountamount = discount.getAmount().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal totalDiscounted = originalTicketPrice.multiply(discountamount);
            BigDecimal finalPricePercentage = originalTicketPrice.subtract(totalDiscounted).setScale(2,
                    RoundingMode.HALF_UP);
            return finalPricePercentage;
        } else {
            BigDecimal discountamount = discount.getAmount();
            BigDecimal finalPrice = originalTicketPrice.subtract(discountamount);
            if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
                return BigDecimal.ZERO;
            }
            return finalPrice.setScale(2, RoundingMode.HALF_UP);
            // return finalPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO :
            // finalPrice.setScale(2, RoundingMode.HALF_UP);
        }
    }
}
