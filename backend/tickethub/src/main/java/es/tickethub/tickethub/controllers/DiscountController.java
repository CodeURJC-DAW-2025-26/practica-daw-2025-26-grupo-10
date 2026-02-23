package es.tickethub.tickethub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @PostMapping("/create_discount")
    public String createDiscount(@Valid Discount discount, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "/admin/discounts/create_discount";
        }

        try {
            discountService.createAndEditDiscount(discount);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/discounts/create_discount";
        }

        return "redirect:/admin/discounts/manage_discounts";
    }

    //To get the info of the saved discount and show it at the form
    @GetMapping("/edit_discount/{discountID}")
    public String showDiscount(@PathVariable Long discountID, Model model) {
        Discount discount = discountService.findById(discountID);
        model.addAttribute("discount", discount);
        return "/admin/discounts/create_discount";
    }

    //To save the discount edited
    @PostMapping("/edit_discount/{discountID}")
    public String editDiscount(@Valid Discount discount, BindingResult result, Model model) {
        Discount editedDiscount = discount;
        try {
            discountService.createAndEditDiscount(editedDiscount);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/discounts/create_discount";
        }
        return "redirect:/admin/discounts/manage_discounts";
    }

    @DeleteMapping("/delete_discount/{discountID}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteDiscount(@PathVariable Long discountID) {
        discountService.deleteDiscount(discountID);
        System.out.println("DESCUENTO NÚMERO " + discountID + " BORRADO");
        return "/admin/discounts/manage_discounts";
    }
}
