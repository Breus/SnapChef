package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Set;

@ApplicationScoped
public class UserService {
    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user with the provided authorName, email address, and passwordadas
     *
     * @param username the authorName of the new user
     * @param emailAddress the email address of the new user
     * @param password the password for the new user
     * @throws UserRegistrationException if there is an error during registration
     */
    public UserAccount registerUser(String username, String emailAddress, String password)
            throws UserRegistrationException {
        if (userRepository.findByEmail(emailAddress) != null) {
            throw new UserRegistrationValidationException("Email address is already in use.");
        }
        if (userRepository.findByUsername(username) != null) {
            throw new UserRegistrationValidationException("Username is already in use.");
        }
        String hashPassword = BcryptUtil.bcryptHash(password);
        return userRepository.createUser(username, hashPassword, emailAddress, Set.of(UserRole.user));
    }
}
