package es.tickethub.tickethub.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin extends User {

    public Admin(){
        // Constructor for the Database
    }

    // Constructor of the class
    public Admin(String email, String username, String password) {
        super(email, username, password, true); // admin = true
    }
}