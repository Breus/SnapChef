package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository {
    private final RoleRepository roleRepository;

    @Inject
    public UserRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UserAccount createUser(String username, String passwordHash, String emailAddress, Set<UserRole> roles) {
        UserAccountEntity user = new UserAccountEntity();
        user.username = username;
        user.passwordHash = passwordHash;
        user.emailAddress = emailAddress;
        user.roles = roles.stream().map(roleRepository::getRoleEntity).collect(Collectors.toSet());
        user.persist();
        return toUserAccount(user);
    }

    @Nullable
    public UserAccount findById(Long id) {
        UserAccountEntity user = UserAccountEntity.findById(id);
        return user == null ? null : toUserAccount(user);
    }

    @Nullable
    public UserAccount findByUsername(String username) {
        UserAccountEntity user = UserAccountEntity.find("username", username).firstResult();
        return user == null ? null : toUserAccount(user);
    }

    @Nullable
    public UserAccount findByEmail(String emailAddress) {
        UserAccountEntity user =
                UserAccountEntity.find("emailAddress", emailAddress).firstResult();
        return user == null ? null : toUserAccount(user);
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
