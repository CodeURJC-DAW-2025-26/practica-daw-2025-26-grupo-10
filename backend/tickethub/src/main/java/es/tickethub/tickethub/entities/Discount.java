package es.tickethub.tickethub.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountID;

    @Column(unique = true)
    private String discountName;

    /* To indicate the if ammount to be discounted is a percentage or a specific money ammount */
    @Column(nullable = false)
    private Boolean percentage; 

    /* precision is for the total of the numbers, scale for the number of decimals*/
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal ammount;

    public Discount() {}    //Constructor for the Database

    // Constructor of the class
    public Discount(String discountName, BigDecimal ammount, Boolean percentage) {
        this.discountName = discountName;
        this.ammount = ammount;
        this.percentage = percentage;
    }
}
