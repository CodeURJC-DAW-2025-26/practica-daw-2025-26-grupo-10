package es.tickethub.tickethub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tickethub.tickethub.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{
    
}
