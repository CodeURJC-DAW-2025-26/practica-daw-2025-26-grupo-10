package es.tickethub.tickethub.services;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.repositories.ClientRepository;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public void registeClient( String name, String email, String surname,String password, String passWordConfirmation){
        if(!password.equals(passWordConfirmation)){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Las contraseñas no coinciden");
        }
        boolean existClient =  clientRepository.existsByEmail(email);
        if(existClient){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este correo electrónico ya está en uso");
        }

        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setSurname(surname);
        client.setPassword(password);
        client.setAdmin(false);
        client.setCoins(BigDecimal.ZERO);
        clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client getClientById(Long clientID){
        Optional<Client> clientOptional = clientRepository.findById(clientID);
        if(clientOptional.isPresent()){
            return clientOptional.get();
        } 
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    @Transactional
    //TODO: llamar al service de imagen para poder actualizar la imagen si es que se manda
    public void updateClient(Long clientID,Client clientUpdated){
        Optional<Client> clientOptional = clientRepository.findById(clientID);
        if(clientOptional.isPresent()){
            Client client = clientOptional.get();
            client.setName(clientUpdated.getName());
            client.setSurname(clientUpdated.getSurname());
            client.setUsername(clientUpdated.getUsername());
            client.setEmail(clientUpdated.getEmail());
            client.setPhone(clientUpdated.getPhone());
            client.setAge(clientUpdated.getAge());
            client.setVersion(clientUpdated.getVersion());
            clientRepository.save(client);//in case of missmatching versions thorws an exception: ObjectOptimisticLockingFailureException
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    @Transactional
    public void changePassword(Long clientID, String oldPassword,String newPassword, String newPasswordConfirmation){
        Client client = clientRepository.findById(clientID)
                                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        if(!client.getPassword().equals(oldPassword)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La contraseña actual es incorrecta");
        }

        if(!newPassword.equals(newPasswordConfirmation)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Las contraseñas nuevas no coinciden");
        }

        client.setPassword(newPassword);
        clientRepository.save(client);
    }

}
