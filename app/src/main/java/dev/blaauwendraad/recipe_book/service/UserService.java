package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user with the provided username, email address, and password.
     *
     * @param username the username of the new user
     * @param emailAddress the email address of the new user
     * @param password the password for the new user
     * @throws UserRegistrationException if there is an error during registration
     */
    public void registerUser(String username, String emailAddress, String password) throws UserRegistrationException {
        if (userRepository.findByEmail(emailAddress) != null) {
            throw new UserRegistrationException("Email address is already in use.");
        }
        if (userRepository.findByUsername(username) != null) {
            throw new UserRegistrationException("Username is already in use.");
        }
        String hashPassword = BcryptUtil.bcryptHash(password);
        userRepository.createUser(username, hashPassword, emailAddress);
    }
}
