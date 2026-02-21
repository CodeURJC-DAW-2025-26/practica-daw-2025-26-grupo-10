package es.tickethub.tickethub.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.User;
import es.tickethub.tickethub.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    

}
