package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.entity.RecipeEntity;
import dev.blaauwendraad.recipe_book.service.model.Author;
import dev.blaauwendraad.recipe_book.service.model.Ingredient;
import dev.blaauwendraad.recipe_book.service.model.PreparationStep;
import dev.blaauwendraad.recipe_book.service.model.Recipe;
import dev.blaauwendraad.recipe_book.service.model.RecipeSummary;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

@ApplicationScoped
public class RecipeService {

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
}
