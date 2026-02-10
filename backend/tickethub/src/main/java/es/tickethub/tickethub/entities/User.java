package es.tickethub.tickethub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean admin;

    /* Constructor for the Database*/
    public User() {
    }

    // Constructor of the class
    public User(String email, String username, String password, Boolean admin) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.admin = admin; 
    }
}
