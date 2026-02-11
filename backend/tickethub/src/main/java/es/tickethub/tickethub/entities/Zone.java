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
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    public Zone() {} /* Constructor for the Database*/

    // Constructor of the class
    public Zone(String name, Integer capacity, BigDecimal price) {
        this.name = name;
        this.capacity = capacity;
        this.price = price;
    }
}