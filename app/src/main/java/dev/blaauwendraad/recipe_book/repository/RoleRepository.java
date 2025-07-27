package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.RoleEntity;
import dev.blaauwendraad.recipe_book.data.model.RoleName;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository {
    public RoleEntity getRoleEntity(UserRole role) {
        return switch (role) {
            case administrator -> getAdminRole();
            case user -> getUserRole();
        };
    }

    public RoleEntity findByName(RoleName name) {
        return RoleEntity.find("roleName", name).firstResult();
    }

    private RoleEntity getUserRole() {
        return findByName(RoleName.user);
    }

    private RoleEntity getAdminRole() {
        return findByName(RoleName.admin);
    }
}
