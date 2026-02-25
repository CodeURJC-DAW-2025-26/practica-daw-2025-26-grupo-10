package es.tickethub.tickethub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<Client> findByEmail(String email);
}
