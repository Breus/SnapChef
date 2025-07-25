package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.RoleEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository {
    @Transactional
    public UserAccount createUser(String username, String passwordHash, String emailAddress, Set<RoleEntity> roles) {
        UserAccountEntity user = new UserAccountEntity();
        user.username = username;
        user.passwordHash = passwordHash;
        user.emailAddress = emailAddress;
        user.roles = roles;
        user.persist();

        return toUserAccount(user);
    }

    public UserAccount findById(Long id) {
        UserAccountEntity user = UserAccountEntity.findById(id);
        if (user == null) {
            return null;
        }
        return toUserAccount(user);
    }

    public UserAccount findByUsername(String username) {
        UserAccountEntity user = UserAccountEntity.find("username", username).firstResult();
        if (user == null) {
            return null;
        }
        return toUserAccount(user);
    }

    public UserAccount findByEmail(String emailAddress) {
        UserAccountEntity user =
                UserAccountEntity.find("emailAddress", emailAddress).firstResult();
        if (user == null) {
            return null;
        }
        return toUserAccount(user);
    }

    private static UserAccount toUserAccount(UserAccountEntity userAccountEntity) {
        return new UserAccount(
                userAccountEntity.id,
                userAccountEntity.username,
                userAccountEntity.emailAddress,
                userAccountEntity.passwordHash,
                userAccountEntity.roles.stream()
                        .map(role -> switch (role.roleName) {
                            case admin -> UserRole.administrator;
                            case user -> UserRole.user;
                        })
                        .collect(Collectors.toSet()));
    }
}
