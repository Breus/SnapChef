package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import java.util.stream.Collectors;

public final class UserAccountConverter {

    private UserAccountConverter() {
        // Utility class, hide constructor.
    }

    public static UserAccount toUserAccount(UserAccountEntity userAccountEntity) {
        return new UserAccount(
                userAccountEntity.id,
                userAccountEntity.username,
                userAccountEntity.emailAddress,
                userAccountEntity.passwordHash,
                userAccountEntity.roles.stream()
                        .map(role -> switch (role.roleName) {
                            case admin -> UserRole.admin;
                            case user -> UserRole.user;
                        })
                        .collect(Collectors.toSet()));
    }
}
