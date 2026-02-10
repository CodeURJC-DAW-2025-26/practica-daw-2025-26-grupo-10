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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    public Discount() {}    //Constructor for the Database

    // Constructor of the class
    public Discount(String code, BigDecimal percentage) {
        this.code = code;
        this.percentage = percentage;
    }
}
