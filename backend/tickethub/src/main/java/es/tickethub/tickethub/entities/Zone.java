package es.tickethub.tickethub.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Min(1) //you cannot buy 0 tickets
    private Integer capacity;

    @Column(nullable = false, precision = 8, scale = 2)
    @DecimalMin("0.0") //not 0,1 due to discounts (possible -20 $ discount)
    private BigDecimal price;

    public Zone() {} /* Constructor for the Database*/

    // Constructor of the class
    public Zone(String name, Integer capacity, BigDecimal price) {
        this.name = name;
        this.capacity = capacity;
        this.price = price;
    }
}