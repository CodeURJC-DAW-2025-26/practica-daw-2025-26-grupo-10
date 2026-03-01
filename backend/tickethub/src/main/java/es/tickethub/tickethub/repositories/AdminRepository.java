package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.tickethub.tickethub.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
}
