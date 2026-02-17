package es.tickethub.tickethub.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.services.DiscountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public List<Discount> findAll() {
        return discountService.getAllDiscounts();
    }
    
    @GetMapping
    public Discount getDiscount(@RequestParam String discountName) {
        Discount discount = discountService.getDiscountByName(discountName);
        return discount;
    }

    @PostMapping
    public Discount save(@RequestBody Discount discount) {
        return discountService.createDiscount(discount);
    }

    @DeleteMapping("/{name}")
    public void delete(@PathVariable String name){
        discountService.deleteDiscount(name);
    }
    
}
