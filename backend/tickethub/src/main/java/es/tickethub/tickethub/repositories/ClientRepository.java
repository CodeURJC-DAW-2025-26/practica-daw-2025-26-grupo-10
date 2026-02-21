package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {
    
}
