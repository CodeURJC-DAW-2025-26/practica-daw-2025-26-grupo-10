package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.services.DiscountService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired DiscountService discountService;

    // Show all discounts
    @GetMapping
    public String listDiscounts(Model model) {
        model.addAttribute("discounts", discountService.getAllDiscounts());
        return "discounts";
    }

    // New discount form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("discount", new Discount());
        return "create-discount"; 
    }

    // Creation of new discount
    @PostMapping
    public String createDiscount(@Valid Discount discount, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "create-discount";
        }

        try {
            discountService.createDiscount(discount);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-discount";
        }

        return "redirect:/discounts";
    }

    @GetMapping("/{name}")
    public String showDiscount(@PathVariable String name, Model model) {
        Discount discount = discountService.getDiscountByName(name);
        model.addAttribute("discount", discount);
        return "discount";
    }

    @GetMapping("/delete/{name}")
    public String deleteDiscount(@PathVariable String name) {
        discountService.deleteDiscount(name);
        return "redirect:/discounts";
    }
}
