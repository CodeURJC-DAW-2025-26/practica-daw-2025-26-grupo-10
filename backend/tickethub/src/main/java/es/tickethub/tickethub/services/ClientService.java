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
import es.tickethub.tickethub.repositories.ImageRepository;

/**
 * Service class responsible for managing clients.
 * Provides methods for registration, updating, password changes, and retrieval of clients.
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new client with the provided information.
     * Validates password confirmation and uniqueness of email/username.
     *
     * @param name Client's first name
     * @param email Client's email address
     * @param surname Client's last name
     * @param password Client's password
     * @param passWordConfirmation Confirmation of the password
     * @param username Desired username
     * @throws ResponseStatusException If passwords do not match or email/username already exist
     */
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

    /**
     * Changes the password of an existing client.
     * Validates old password and matches new password confirmation.
     *
     * @param email Client's email address
     * @param oldPassword Current password
     * @param newPassword New password
     * @param newPasswordConfirmation Confirmation of the new password
     * @throws ResponseStatusException If old password is incorrect or new passwords do not match
     */
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

    /**
     * Updates client details, optionally including profile image.
     * Ensures email uniqueness if changed.
     *
     * @param loggedEmail Email of the logged-in client
     * @param updatedClient Client object containing updated data
     * @param imageFile Optional profile image file
     * @return Updated Client entity
     * @throws IOException If an error occurs while processing the image file
     * @throws ResponseStatusException If email is already in use
     */
    @Transactional
    public Client updateClient(String loggedEmail, Client updatedClient, MultipartFile imageFile) throws IOException {
        Client client = findClientOrThrowByEmail(loggedEmail);
        if (!client.getEmail().equals(updatedClient.getEmail())
                && clientRepository.existsByEmail(updatedClient.getEmail())) {
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
        return saveClient(client);
    }

    /**
     * Updates an existing client using form data.
     *
     * @param id ID of the client to update
     * @param formClient Client object containing updated data
     */
    @Transactional
    public void updateUser(Long id, Client formClient) {
        Client client = findClientOrThrowById(id);
        copyClientFields(client, formClient);
        saveClient(client);
    }

    /**
     * Retrieves a client by ID.
     *
     * @param id Client ID
     * @return Client entity
     * @throws ResponseStatusException If client is not found
     */
    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        return findClientOrThrowById(id);
    }

    /**
     * Retrieves a client by email.
     *
     * @param email Client's email
     * @return Client entity
     * @throws ResponseStatusException If client is not found
     */
    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        return findClientOrThrowByEmail(email);
    }

    /**
     * Finds a client by email, returning an Optional.
     *
     * @param email Client's email
     * @return Optional containing the Client if found
     */
    @Transactional(readOnly = true)
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    /**
     * Returns a list of all non-admin clients.
     *
     * @return List of clients who are not admins
     */
    @Transactional(readOnly = true)
    public List<Client> getNonAdminClients() {
        return clientRepository.findAll().stream()
                .filter(c -> !c.getAdmin())
                .toList();
    }

    /**
     * Finds a client by ID or throws a 404 exception if not found.
     *
     * @param id Client ID
     * @return Client entity
     * @throws ResponseStatusException If client is not found
     */
    private Client findClientOrThrowById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    /**
     * Finds a client by email or throws a 404 exception if not found.
     *
     * @param email Client's email
     * @return Client entity
     * @throws ResponseStatusException If client is not found
     */
    private Client findClientOrThrowByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    /**
     * Saves a client entity to the repository.
     *
     * @param client Client entity to save
     * @return Saved Client entity
     */
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Copies all relevant fields from the source client to the target client.
     *
     * @param target Client entity to be updated
     * @param source Client entity containing updated data
     */
    private void copyClientFields(Client target, Client source) {
        target.setName(source.getName());
        target.setSurname(source.getSurname());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setAge(source.getAge());
        target.setVersion(source.getVersion());
        target.setProfileImage(source.getProfileImage());
    }

    public void deleteClientImage(Client client, Long imageID) {
        imageRepository.deleteById(imageID);
        client.setProfileImage(null);
    }
}