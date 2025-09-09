package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.IngredientEntity;
import dev.blaauwendraad.recipe_book.data.model.PreparationStepEntity;
import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.model.Author;
import dev.blaauwendraad.recipe_book.service.model.Ingredient;
import dev.blaauwendraad.recipe_book.service.model.PreparationStep;
import dev.blaauwendraad.recipe_book.service.model.Recipe;
import dev.blaauwendraad.recipe_book.service.model.RecipeSummary;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeService {
    private final UserRepository userRepository;

    @Inject
    public RecipeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<RecipeSummary> getAllRecipeSummaries() {
        return RecipeEntity.<RecipeEntity>listAll().stream()
                .map(recipeEntity -> new RecipeSummary(
                        recipeEntity.id,
                        recipeEntity.title,
                        recipeEntity.description,
                        recipeEntity.author == null
                                ? null
                                : new Author(recipeEntity.author.id, recipeEntity.author.username)))
                .collect(Collectors.toList());
    }

    @Nullable
    public Recipe getRecipeById(Long id) {
        RecipeEntity recipeEntity = RecipeEntity.findById(id);
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
        return persistRecipeEntity(null, userAccountEntity, title, description, ingredients, preparationSteps);
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
        recipeEntity.delete();
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

        persistRecipeEntity(
                existingRecipeEntity, existingRecipeEntity.author, title, description, ingredients, preparationSteps);
    }

    @Transactional
    Long persistRecipeEntity(
            @Nullable RecipeEntity existingRecipeEntity,
            UserAccountEntity userAccountEntity,
            String title,
            String description,
            List<Ingredient> ingredients,
            List<PreparationStep> preparationSteps) {
        if (existingRecipeEntity != null) {
            for (var ingredient : existingRecipeEntity.ingredients) {
                ingredient.delete();
            }
            for (var preparationStep : existingRecipeEntity.preparationSteps) {
                preparationStep.delete();
            }
        }
        var recipeEntity = existingRecipeEntity != null ? existingRecipeEntity : new RecipeEntity();
        recipeEntity.title = title;
        recipeEntity.description = description;
        if (userAccountEntity == null && recipeEntity.author == null) {
            throw new IllegalArgumentException("Recipe must have an author");
        } else if (userAccountEntity != null) {
            recipeEntity.author = userAccountEntity;
        }
        List<IngredientEntity> ingredientsEntities = new ArrayList<>();
        for (int position = 0; position < ingredients.size(); position++) {
            var ingredient = ingredients.get(position);
            var ingredientEntity = new IngredientEntity();
            ingredientEntity.recipe = recipeEntity;
            ingredientEntity.name = ingredient.name();
            ingredientEntity.position = position;
            ingredientEntity.quantity = ingredient.quantity();
            ingredientsEntities.add(ingredientEntity);
        }
        recipeEntity.ingredients = ingredientsEntities;

        List<PreparationStepEntity> preparationStepEntities = new ArrayList<>();
        for (int position = 0; position < preparationSteps.size(); position++) {
            var step = preparationSteps.get(position);
            var preparationStepEntity = new PreparationStepEntity();
            preparationStepEntity.recipe = recipeEntity;
            preparationStepEntity.description = step.description();
            preparationStepEntity.position = position;
            preparationStepEntities.add(preparationStepEntity);
        }
        recipeEntity.preparationSteps = preparationStepEntities;
        recipeEntity.persist();
        recipeEntity.persistAndFlush();
        return recipeEntity.id;
    }
}
