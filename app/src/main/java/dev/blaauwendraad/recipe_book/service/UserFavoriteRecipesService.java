package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UserFavoriteRecipesService {
    private final UserRepository userRepository;

    @Inject
    public UserFavoriteRecipesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Long> getUserFavoriteRecipes(Long userId) {
        return userRepository.findById(userId).favoriteRecipes.stream()
                .map(r -> r.id)
                .toList();
    }

    public List<Long> addUserFavoriteRecipe(Long userId, Long recipeId) {
        return userRepository.addFavoriteRecipe(userId, recipeId).favoriteRecipes.stream()
                .map(r -> r.id)
                .toList();
    }

    public List<Long> removeUserFavoriteRecipe(Long userId, Long recipeId) {
        return userRepository.removeFavoriteRecipe(userId, recipeId).favoriteRecipes.stream()
                .map(r -> r.id)
                .toList();
    }
}
