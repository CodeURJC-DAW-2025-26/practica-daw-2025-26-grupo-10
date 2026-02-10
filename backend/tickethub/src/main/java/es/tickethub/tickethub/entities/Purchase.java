package es.tickethub.tickethub.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Purchase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseID;

    /* References the ticket entity to make the effect that if the purchase is deleted the tickets too
        The join column is to represent a OneToMany relation. You have to put the name of the ID of the other Entity*/
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id")
    private List<Ticket> tickets = new ArrayList<>();

    /* Needed to associate many purchases to one session*/
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    /* Needed to associate the mapping at the client entity to the purchases*/
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    /* To initialize the purchase total price to 0*/
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public Purchase() {
        /* The constructor for the database*/
    }

    // Constructor of the class
    public Purchase(Long purchaseID, List<Ticket> tickets, Session session) {
        this.purchaseID = purchaseID;
        this.tickets = tickets;
        this.session = session;
        if (tickets != null) {  /* If there are tickets for each ticket the price is added to the purchase total price*/
            for (Ticket ticket : tickets) {
                this.totalPrice = this.totalPrice.add(ticket.getPrice());
            }
        }
    }
}
