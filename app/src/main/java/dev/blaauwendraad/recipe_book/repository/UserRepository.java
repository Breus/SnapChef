package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository {
    @Transactional
    public UserAccountEntity createUser(String username, String passwordHash, String emailAddress) {
        UserAccountEntity user = new UserAccountEntity();
        user.username = username;
        user.passwordHash = passwordHash;
        user.emailAddress = emailAddress;
        user.persist();
        return user;
    }

    public UserAccountEntity findByUsername(String username) {
        return UserAccountEntity.find("username", username).firstResult();
    }

    public UserAccountEntity findByEmail(String emailAddress) {
        return UserAccountEntity.find("emailAddress", emailAddress).firstResult();
    }
}
