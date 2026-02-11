package es.tickethub.tickethub.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Discount {

    @Id
    private String discountName;

    @Column(nullable = false)
    private BigDecimal ammount; /* To indicate the ammount to be discounted to the original price*/

    /* precision is for the total of the numbers, scale for the number of decimals*/
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    public Discount() {}    //Constructor for the Database

    // Constructor of the class
    public Discount(String discountName, BigDecimal ammount, BigDecimal percentage) {
        this.discountName = discountName;
        this.ammount = ammount;
        this.percentage = percentage;
    }
}
