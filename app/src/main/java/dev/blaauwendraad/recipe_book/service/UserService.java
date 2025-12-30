package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserAuthenticationException;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {
    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getEmail(Long userId) {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new NotFoundException("User with userId " + userId + " does not exist");
        }
        return userAccountEntity.emailAddress;
    }

    @Transactional
    public void updateEmail(Long userId, String newEmail, String currentPassword) throws UserAuthenticationException {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new UserAuthenticationException("No user account found for the provided user ID.");
        }
        if (!BcryptUtil.matches(currentPassword, userAccountEntity.passwordHash)) {
            throw new UserAuthenticationException("Invalid password provided.");
        }
        userAccountEntity.emailAddress = newEmail;
        userRepository.persist(userAccountEntity);
    }

    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword, String confirmPassword)
            throws UserAuthenticationException {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new UserAuthenticationException("No user account found for the provided user ID.");
        }
        if (!BcryptUtil.matches(currentPassword, userAccountEntity.passwordHash)) {
            throw new UserAuthenticationException("Invalid password provided.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new UserAuthenticationException("New password and confirmation password do not match.");
        }
        userAccountEntity.passwordHash = BcryptUtil.bcryptHash(newPassword);
        userRepository.persist(userAccountEntity);
    }
}
