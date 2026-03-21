package es.tickethub.tickethub.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.tickethub.tickethub.entities.Client;
import es.tickethub.tickethub.entities.Image;
import es.tickethub.tickethub.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerClient(String name, String email, String surname, String password, String passWordConfirmation,
            String username) {

        if (!password.equals(passWordConfirmation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (clientRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este nombre de usuario ya está en uso");
        }

        Client client = clientRepository.findByEmail(email).orElse(new Client());
        client.setEmail(email);

        if (!client.getPassword().equals("")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este correo electrónico ya pertenece a una cuenta");
        }

        client.setName(name);
        client.setSurname(surname);
        client.setUsername(username);
        client.setPassword(passwordEncoder.encode(password));
        client.setAdmin(false);

        saveClient(client);
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword, String newPasswordConfirmation) {
        Client client = findClientOrThrowByEmail(email);

        if (!passwordEncoder.matches(oldPassword, client.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta");
        }

        if (!newPassword.equals(newPasswordConfirmation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas nuevas no coinciden");
        }

        client.setPassword(passwordEncoder.encode(newPassword));
        saveClient(client);
    }

    @Transactional
    public void updateClient(String loggedEmail, Client updatedClient, MultipartFile imageFile) throws IOException {
        Client client = findClientOrThrowByEmail(loggedEmail);

        if (!client.getEmail().equals(updatedClient.getEmail()) &&
            clientRepository.existsByEmail(updatedClient.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese correo electrónico ya está en uso");
        }

        copyClientFields(client, updatedClient);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Image newImage = new Image();
                newImage.setImageCode(new SerialBlob(imageFile.getBytes()));
                client.setProfileImage(newImage);
            } catch (SQLException e) {
                throw new IOException("Error al procesar los bytes de la imagen");
            }
        }

        saveClient(client);
    }

    @Transactional
    public void updateUser(Long id, Client formClient) {
        Client client = findClientOrThrowById(id);
        copyClientFields(client, formClient);
        saveClient(client);
    }

    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        return findClientOrThrowById(id);
    }

    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        return findClientOrThrowByEmail(email);
    }

    @Transactional(readOnly = true)
    public Client getLoggedClient(String email) {
        return findClientOrThrowByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Client> getNonAdminClients() {
        return clientRepository.findAll().stream()
                .filter(c -> !c.getAdmin())
                .toList();
    }

    // ======================
    // REST METHODS
    // ======================

    @Transactional
    public Client updateClientREST(String email, Client updatedClient) {
        Client client = findClientOrThrowByEmail(email);

        client.setName(updatedClient.getName());
        client.setSurname(updatedClient.getSurname());
        client.setUsername(updatedClient.getUsername());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setAge(updatedClient.getAge());
        client.setProfileImage(updatedClient.getProfileImage());

        saveClient(client);
        return client;
    }

    private Client findClientOrThrowById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    private Client findClientOrThrowByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    private void copyClientFields(Client target, Client source) {
        target.setName(source.getName());
        target.setSurname(source.getSurname());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setAge(source.getAge());
        target.setVersion(source.getVersion());
    }
}