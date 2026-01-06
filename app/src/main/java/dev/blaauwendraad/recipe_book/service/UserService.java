package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserAuthenticationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
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

    public UserAccount getUser(Long userId) {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new NotFoundException("User with given userId does not exist");
        }
        return UserAccountConverter.toUserAccount(userAccountEntity);
    }

    @Transactional
    public void updateEmail(Long userId, String newEmail, String currentPassword) throws UserAuthenticationException {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new NotFoundException("User with given userId does not exist");
        }
        if (!BcryptUtil.matches(currentPassword, userAccountEntity.passwordHash)) {
            throw new UserAuthenticationException("Invalid password provided.");
        }
        UserAccountEntity existingUserWithSameEmail = userRepository.findByEmail(newEmail);
        if (existingUserWithSameEmail != null && !existingUserWithSameEmail.id.equals(userId)) {
            throw new UserAuthenticationException("The provided new email address is already in use.");
        }
        userAccountEntity.emailAddress = newEmail;
        userRepository.persist(userAccountEntity);
    }

    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword)
            throws UserAuthenticationException {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new NotFoundException("User with given userId does not exist");
        }
        if (!BcryptUtil.matches(currentPassword, userAccountEntity.passwordHash)) {
            throw new UserAuthenticationException("Current password is incorrect.");
        }
        userAccountEntity.passwordHash = BcryptUtil.bcryptHash(newPassword);
        userRepository.persist(userAccountEntity);
    }
}
