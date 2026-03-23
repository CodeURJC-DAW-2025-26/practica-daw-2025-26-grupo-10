package es.tickethub.tickethub.rest_controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.tickethub.tickethub.dto.DiscountBasicDTO;
import es.tickethub.tickethub.dto.DiscountCreateDTO;
import es.tickethub.tickethub.dto.DiscountDTO;
import es.tickethub.tickethub.entities.Discount;
import es.tickethub.tickethub.mappers.DiscountMapper;
import es.tickethub.tickethub.services.DiscountService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/admin/discounts")
public class DiscountRestController {

    @Autowired DiscountService discountService;
    @Autowired DiscountMapper discountMapper;

    @GetMapping
    public Page<DiscountBasicDTO> getDiscounts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return discountService.getDiscountsPage(page, size).map(discountMapper::toBasicDTO);
    }

    @GetMapping("/{discountID}")
    public ResponseEntity<DiscountBasicDTO> getDiscount(@PathVariable Long discountID) {
        Discount discount = discountService.findById(discountID);
        return ResponseEntity.ok(discountMapper.toBasicDTO(discount));
    }

    @PostMapping("/")
    public ResponseEntity<DiscountDTO> createDiscount(@Valid @RequestBody DiscountCreateDTO discountCreateDTO) {
        Discount newDiscount = discountMapper.toEntity(discountCreateDTO);
        discountService.save(newDiscount);

        URI location = fromCurrentRequest().path("/{discountID}").buildAndExpand(newDiscount.getDiscountID()).toUri();

        return ResponseEntity.created(location).body(discountMapper.toDTO(newDiscount));
    }

    @PutMapping("/{discountID}")
    public DiscountDTO updateDiscount(@Valid @RequestBody DiscountBasicDTO discountUpdateDTO, @PathVariable Long discountID) {
        Discount existingDiscount = discountService.findById(discountID);
        discountMapper.updateEntityFromBasicDto(discountUpdateDTO, existingDiscount);
        Discount updated = discountService.editDiscount(existingDiscount, discountID);

        return discountMapper.toDTO(updated);
    }

    @DeleteMapping("/{discountID}")
    public DiscountDTO deleteDiscount (@PathVariable Long discountID) {
        Discount discount = discountService.findById(discountID);
        discountService.deleteDiscount(discountID);
        return discountMapper.toDTO(discount);
    }    
}
