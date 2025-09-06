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
            Long authorId,
            List<Ingredient> ingredients,
            List<PreparationStep> steps) {
        UserAccountEntity userAccountEntity = userRepository.findById(authorId);
        if (userAccountEntity == null) {
            throw new IllegalArgumentException("Author with userId " + authorId + " does not exist");
        }
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.title = title;
        recipeEntity.description = description;
        recipeEntity.author = userAccountEntity;

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
        for (int position = 0; position < steps.size(); position++) {
            var step = steps.get(position);
            var preparationStepEntity = new PreparationStepEntity();
            preparationStepEntity.recipe = recipeEntity;
            preparationStepEntity.description = step.description();
            preparationStepEntity.position = position;
            preparationStepEntities.add(preparationStepEntity);
        }
        recipeEntity.preparationSteps = preparationStepEntities;
        recipeEntity.persist();
        return recipeEntity.id;
    }

    @Transactional
    public void removeRecipe(Long recipeId, Long upn) {
        UserAccountEntity userAccount = userRepository.findById(upn);
        if (userAccount == null) {
            throw new IllegalArgumentException("User with userId " + upn + " does not exist");
        }
        RecipeEntity recipeEntity = RecipeEntity.findById(recipeId);
        if (recipeEntity == null) {
            throw new IllegalArgumentException("Recipe with userId " + recipeId + " does not exist");
        }
        if (recipeEntity.author == null || !recipeEntity.author.id.equals(upn)) {
            throw new IllegalArgumentException(
                    "User with userId " + upn + " is not the author of the recipe with userId " + recipeId);
        }
        recipeEntity.delete();
    }
}
