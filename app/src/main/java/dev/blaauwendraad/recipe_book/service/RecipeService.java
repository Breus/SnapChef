package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.RecipeRepository;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.resource.model.RecipeSummariesFilter;
import dev.blaauwendraad.recipe_book.service.model.Author;
import dev.blaauwendraad.recipe_book.service.model.Ingredient;
import dev.blaauwendraad.recipe_book.service.model.PreparationStep;
import dev.blaauwendraad.recipe_book.service.model.Recipe;
import dev.blaauwendraad.recipe_book.service.model.RecipeSummary;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Inject
    public RecipeService(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeSummary> getAllRecipeSummaries(
            RecipeSummariesFilter recipeSummariesFilter, @Nullable Long userId) {
        switch (recipeSummariesFilter) {
            case ALL -> {
                return recipeRepository.listAll().stream()
                        .map(this::toRecipeSummary)
                        .toList();
            }
            case MY -> {
                if (userId == null) {
                    throw new IllegalArgumentException("UserId must be provided when using the MY filter");
                }
                return recipeRepository.listRecipesAuthoredBy(userId).stream()
                        .map(this::toRecipeSummary)
                        .toList();
            }
            case FAVORITES -> {
                if (userId == null) {
                    throw new IllegalArgumentException("UserId must be provided when using the MY filter");
                }
                return recipeRepository.listRecipesByIds(userRepository.listFavoriteRecipeIds(userId)).stream()
                        .map(this::toRecipeSummary)
                        .toList();
            }
            case null, default -> throw new IllegalArgumentException("Invalid recipe summaries filter provided");
        }
    }

    private RecipeSummary toRecipeSummary(RecipeEntity recipeEntity) {
        return new RecipeSummary(
                recipeEntity.id,
                recipeEntity.title,
                recipeEntity.description,
                recipeEntity.author == null ? null : new Author(recipeEntity.author.id, recipeEntity.author.username));
    }

    @Nullable
    public Recipe getRecipeById(Long recipeId) {
        RecipeEntity recipeEntity = recipeRepository.findById(recipeId);
        if (recipeEntity == null) {
            return null;
        }
        return new Recipe(
                recipeEntity.id,
                recipeEntity.title,
                recipeEntity.description,
                recipeEntity.author == null ? null : new Author(recipeEntity.author.id, recipeEntity.author.username),
                recipeEntity.ingredients.stream()
                        .map(ingredient -> new Ingredient(ingredient.name, ingredient.quantity))
                        .collect(Collectors.toList()),
                recipeEntity.preparationSteps.stream()
                        .map(step -> new PreparationStep(step.description))
                        .collect(Collectors.toList()));
    }

    @Transactional
    public Long createRecipe(
            String title,
            String description,
            Long userId,
            List<Ingredient> ingredients,
            List<PreparationStep> preparationSteps) {
        UserAccountEntity userAccountEntity = userRepository.findById(userId);
        if (userAccountEntity == null) {
            throw new IllegalArgumentException("Author with userId " + userId + " does not exist");
        }
        return recipeRepository.persistRecipeEntity(
                null, userAccountEntity, title, description, ingredients, preparationSteps);
    }

    @Transactional
    public void deleteRecipe(Long recipeId, Long userId) {
        UserAccountEntity userAccount = userRepository.findById(userId);
        if (userAccount == null) {
            throw new IllegalArgumentException("User with userId " + userId + " does not exist");
        }
        RecipeEntity recipeEntity = RecipeEntity.findById(recipeId);
        if (recipeEntity == null) {
            throw new IllegalArgumentException("Recipe with recipeId " + recipeId + " does not exist");
        }
        if (recipeEntity.author == null || !recipeEntity.author.id.equals(userId)) {
            throw new IllegalArgumentException(
                    "User with userId " + userId + " is not the author of the recipe with recipeId " + recipeId);
        }
        recipeRepository.deleteById(recipeId);
    }

    @Transactional
    public void updateRecipe(
            Long recipeId,
            String title,
            String description,
            Long userId,
            List<Ingredient> ingredients,
            List<PreparationStep> preparationSteps) {
        UserAccountEntity userAccount = userRepository.findById(userId);
        if (userAccount == null) {
            throw new IllegalArgumentException("User with userId " + userId + " does not exist");
        }
        RecipeEntity existingRecipeEntity = RecipeEntity.findById(recipeId);
        if (existingRecipeEntity == null) {
            throw new IllegalArgumentException("Recipe with recipeId " + recipeId + " does not exist");
        }
        if (existingRecipeEntity.author == null || !existingRecipeEntity.author.id.equals(userId)) {
            throw new IllegalArgumentException(
                    "User with userId " + userId + " is not the author of the recipe with recipeId " + recipeId);
        }
        recipeRepository.persistRecipeEntity(
                existingRecipeEntity, existingRecipeEntity.author, title, description, ingredients, preparationSteps);
    }
}
