package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.services.DiscountService;


@Controller
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public Discount getDiscount(@RequestParam String discountName) {
        Discount discount = discountService.getDiscountByName(discountName);
        return discount;
    }

    @GetMapping
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    
}
