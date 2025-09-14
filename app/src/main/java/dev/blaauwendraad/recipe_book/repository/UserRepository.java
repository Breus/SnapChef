package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserAccountEntity> {
    private final RoleRepository roleRepository;

    @Inject
    public UserRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UserAccountEntity addFavoriteRecipe(Long userId, Long recipeId) {
        UserAccountEntity userAccountEntity = UserAccountEntity.findById(userId);
        if (userAccountEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        RecipeEntity recipeEntity = RecipeEntity.findById(recipeId);
        if (recipeEntity == null) {
            throw new IllegalArgumentException("Recipe not found");
        }
        if (userAccountEntity.favoriteRecipes.contains(recipeEntity)) {
            // do nothing, already a favorite recipe
            return userAccountEntity;
        }
        userAccountEntity.favoriteRecipes.add(recipeEntity);
        userAccountEntity.persist();
        return userAccountEntity;
    }

    @Transactional
    public UserAccountEntity removeFavoriteRecipe(Long userId, Long recipeId) {
        UserAccountEntity userAccountEntity = UserAccountEntity.findById(userId);
        if (userAccountEntity == null) {
            throw new IllegalArgumentException("User not found");
        }
        RecipeEntity recipeEntity = RecipeEntity.findById(recipeId);
        if (recipeEntity == null) {
            throw new IllegalArgumentException("Recipe not found");
        }
        if (!userAccountEntity.favoriteRecipes.contains(recipeEntity)) {
            // do nothing, not a favorite recipe
            return userAccountEntity;
        }
        userAccountEntity.favoriteRecipes.remove(recipeEntity);
        userAccountEntity.persist();
        return userAccountEntity;
    }

    @Transactional
    public UserAccountEntity createUser(
            String username, String passwordHash, String emailAddress, Set<UserRole> roles) {
        UserAccountEntity user = new UserAccountEntity();
        user.username = username;
        user.passwordHash = passwordHash;
        user.emailAddress = emailAddress;
        user.roles = roles.stream().map(roleRepository::getRoleEntity).collect(Collectors.toSet());
        user.persist();
        return user;
    }

    @Nullable
    public UserAccountEntity findByUsername(String username) {
        return UserAccountEntity.find("userName", username).firstResult();
    }

    @Nullable
    public UserAccountEntity findByEmail(String emailAddress) {
        return UserAccountEntity.find("emailAddress", emailAddress).firstResult();
    }
}
