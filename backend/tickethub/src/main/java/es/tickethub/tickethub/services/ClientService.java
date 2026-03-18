package es.tickethub.tickethub.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.dto.ClientDTO;
import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.mappers.ClientMapper;
import es.tickethub.tickethub.repositories.ClientRepository;
import lombok.Getter;

@Service
@Getter
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientMapper clientMapper;

    @Transactional
    public void registerClient(String name, String email, String surname, String password, String passWordConfirmation,
            String username) {
        if (!password.equals(passWordConfirmation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        boolean existClient = clientRepository.existsByUsername(username);
        if (existClient) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este nombre de usuario ya está en uso");
        }

        Optional<Client> client = clientRepository.findByEmail(email);
        Client getClient;

        if (client.isPresent()) {
            getClient = client.get();

            if (!getClient.getPassword().equals("")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Este correo electrónico ya pertenece a una cuenta");
            }
        } else {
            getClient = new Client();
            getClient.setEmail(email);
        }

        getClient.setName(name);
        getClient.setSurname(surname);
        getClient.setUsername(username);
        String encodedPassword = passwordEncoder.encode(password);
        getClient.setPassword(encodedPassword);
        getClient.setAdmin(false);
        getClient.setCoins(BigDecimal.ZERO);
        clientRepository.save(getClient);
    }

    @Transactional(readOnly = true)
    public Client getClientById(Long clientID) {
        Optional<Client> clientOptional = clientRepository.findById(clientID);
        if (clientOptional.isPresent()) {
            return clientOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        Optional<Client> clientOptional = clientRepository.findByEmail(email);
        if (clientOptional.isPresent()) {
            return clientOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
    }

    @Transactional(readOnly = true)
    public Client getLoggedClient(String loggedEmail) {
        return getClientByEmail(loggedEmail);
    }

    @Transactional
    public void updateClient(String loggedEmail, Client clientUpdated, MultipartFile imageFile) throws IOException {
        Client client = clientRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        String nuevoEmail = clientUpdated.getEmail();
        if (!client.getEmail().equals(nuevoEmail)) {
            if (clientRepository.existsByEmail(nuevoEmail)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese correo electrónico ya está en uso");
            }
        }

        client.setName(clientUpdated.getName());
        client.setSurname(clientUpdated.getSurname());
        client.setUsername(clientUpdated.getUsername());
        client.setEmail(clientUpdated.getEmail());
        client.setPhone(clientUpdated.getPhone());
        client.setAge(clientUpdated.getAge());
        client.setVersion(clientUpdated.getVersion());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Image newImage = new Image();
                newImage.setImageCode(new javax.sql.rowset.serial.SerialBlob(imageFile.getBytes()));
                client.setProfileImage(newImage);
            } catch (SQLException e) {
                throw new IOException("Error al procesar los bytes de la imagen");
            }
        }

        clientRepository.save(client);
    }

    @Transactional
    public void changePassword(String loggedEmail, String oldPassword, String newPassword,
            String newPasswordConfirmation) {
        Client client = clientRepository.findByEmail(loggedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        if (!passwordEncoder.matches(oldPassword, client.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
        }

        if (!newPassword.equals(newPasswordConfirmation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas nuevas no coinciden");
        }

        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(client);
    }

    @Transactional
    public void updateUser(Long id, Client formClient) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setName(formClient.getName());
        client.setSurname(formClient.getSurname());
        client.setEmail(formClient.getEmail());

        clientRepository.save(client);
    }

    // ======================
    // REST METHODS
    // ======================

    @Transactional(readOnly = true)
    public ClientDTO getClientDTOById(Long clientID) {
        return clientMapper.toDTO(getClientById(clientID));
    }

    @Transactional(readOnly = true)
    public ClientDTO getLoggedClientDTO(String email) {
        return clientMapper.toDTO(getClientByEmail(email));
    }

    @Transactional
    public ClientDTO updateClientREST(String loggedEmail, ClientDTO dto) {

        Client client = getClientByEmail(loggedEmail);

        client.setName(dto.name());
        client.setSurname(dto.surname());
        client.setUsername(dto.username());
        client.setEmail(dto.email());
        client.setPhone(dto.phone());
        client.setAge(dto.age());

        clientRepository.save(client);

        return clientMapper.toDTO(client);
    }

    @Transactional(readOnly = true)
    public List<Client> getNonAdminClients() {
        return clientRepository.findAll()
                .stream()
                .filter(c -> !c.getAdmin())
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}