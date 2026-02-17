package es.tickethub.tickethub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.repositories.DiscountRepository;

@Service
public class DiscountService {
    @Autowired DiscountRepository discountRepository;

    public Discount getDiscountByName(String discountName){
        Optional <Discount> discountOptional = discountRepository.getByDiscountName(discountName);
        if (discountOptional.isPresent()){
            return discountOptional.get();
        }
        return null;
    }

    /* Get all discounts that exist in the DB */
    public List<Discount> getAllDiscounts(){
        return discountRepository.findAll();
    }
}
