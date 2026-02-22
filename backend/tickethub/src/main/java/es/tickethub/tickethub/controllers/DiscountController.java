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
@RequestMapping("/admin/discounts")
public class DiscountController {

    @Autowired DiscountService discountService;

    // Show all discounts
    @GetMapping("/manage_discounts")
    public String listDiscounts(Model model) {
        model.addAttribute("discounts", discountService.getAllDiscounts());
        return "/admin/discounts/manage_discounts";
    }

    // New discount form
    @GetMapping("/create_discount")
    public String showCreateForm(Model model) {
        
        return "/admin/discounts/create_discount"; 
    }

    // Creation of new discount
    @PostMapping
    public String createDiscount(@Valid Discount discount, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "/admin/create_discount";
        }

        try {
            discountService.createDiscount(discount);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/create_discount";
        }

        return "redirect:/admin/discounts";
    }

    @GetMapping("/edit_discount/{discountID}")
    public String showDiscount(@PathVariable Long discountID, Model model) {
        Discount discount = discountService.findById(discountID);
        model.addAttribute("discount", discount);
        return "/admin/discounts/create_discount";
    }

    @GetMapping("/delete/{discountID}")
    public String deleteDiscount(@PathVariable Long discountID) {
        discountService.deleteDiscount(discountID);
        return "redirect://admin/discounts/manage_discounts";
    }
}
