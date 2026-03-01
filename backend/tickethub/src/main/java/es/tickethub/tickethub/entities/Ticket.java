package es.tickethub.tickethub.entities;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketID;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    @Column(nullable = false)
    private Boolean isActive;

    public Ticket() {
        this.code = UUID.randomUUID().toString();
        this.isActive = true;
    }

    public Ticket(Purchase purchase, Zone zone) {
        this();
        this.purchase = purchase;
        this.zone = zone;
        this.ticketPrice = zone.getPrice();
    }
}