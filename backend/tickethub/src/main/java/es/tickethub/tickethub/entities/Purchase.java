package es.tickethub.tickethub.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseID;

    /* 
    It references the Ticket entity to ensure that if a Purchase is deleted, its associated Tickets are also deleted.
    The @JoinColumn annotation is used to define a One-to-Many relationship. You must specify the name of the ID field of the related entity.
    The orphanRemoval attribute ensures that when an entity instance is removed, all associated child entities are automatically deleted as well.*/


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)

    private List<Ticket> tickets = new ArrayList<>();

    // Needed to associate many purchases to one session
    @ManyToOne //TODO: (fetch = FetchType.LAZY)//for not to load all the session, only its ID, this is for DTOS
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    // Needed to associate the mapping at the client entity to the purchases
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // To initialize the purchase total price to 0
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    // The constructor for the database
    public Purchase() {}

    // Constructor of the class
    public Purchase(List<Ticket> tickets, Session session, Client client) {
        if (tickets != null) {      // If there are tickets we associate them to the attribute and for each ticket the price is added to the purchase total price
            this.tickets = tickets;
            for (Ticket ticket : tickets) {
                this.totalPrice = this.totalPrice.add(ticket.getTicketPrice());
            }
        }
        this.session = session;
        this.client = client;
    }
}