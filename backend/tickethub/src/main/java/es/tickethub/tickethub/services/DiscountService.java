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
import es.tickethub.tickethub.repositories.DiscountRepository;

@Service
public class DiscountService {
    @Autowired DiscountRepository discountRepository;

    public Discount findById(Long discountID){
        Optional <Discount> discountOptional = discountRepository.findById(discountID);
        if (!discountOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado");
        }
        return discountOptional.get();
    }

    /* Get all discounts that exist in the DB */
    public List<Discount> getAllDiscounts(){
        List<Discount> discounts = discountRepository.findAll();
        if (discounts.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay descuentos registrados");
        }
        return discounts;
    }

    /* Use of existsByDiscountName -> no need to bring the discount. We want to check if it already exists */

    public Discount createDiscount(Discount discount){
        if (discountRepository.existsById(discount.getDiscountID())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del descuento ya existe");
        }
        return discountRepository.save(discount);
    }

    public void deleteDiscount(Long discountID){
        Optional<Discount> optionalDiscount = discountRepository.findById(discountID);
        if (!optionalDiscount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado");
        }
        Discount discount = optionalDiscount.get();
        discountRepository.deleteById(discount.getDiscountID()); 
    }

    /* Apply discount differently whether it's a percentage or not */
    public BigDecimal applyDiscount(BigDecimal originalTicketPrice, Long discountID){
        Discount discount = findById(discountID);

        if (discount.getPercentage()){
            BigDecimal discountAmmount = discount.getAmmount().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal totalDiscounted = originalTicketPrice.multiply(discountAmmount);
            BigDecimal finalPricePercentage = originalTicketPrice.subtract(totalDiscounted).setScale(2, RoundingMode.HALF_UP);
            return finalPricePercentage;
        } else {
            BigDecimal discountAmmount = discount.getAmmount();
            BigDecimal finalPrice = originalTicketPrice.subtract(discountAmmount);
            if (finalPrice.compareTo(BigDecimal.ZERO) < 0){
                return BigDecimal.ZERO;
            }
            return finalPrice.setScale(2, RoundingMode.HALF_UP);
            //return finalPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalPrice.setScale(2, RoundingMode.HALF_UP);
        }
    } 
}
