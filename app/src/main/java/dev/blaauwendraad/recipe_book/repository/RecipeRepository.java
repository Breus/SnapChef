package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.IngredientEntity;
import dev.blaauwendraad.recipe_book.data.model.PreparationStepEntity;
import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.service.model.Ingredient;
import dev.blaauwendraad.recipe_book.service.model.PreparationStep;
import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecipeRepository implements PanacheRepository<RecipeEntity> {

    public List<RecipeEntity> listRecipesAuthoredBy(Long userId) {
        return getSession()
                .createQuery("FROM RecipeEntity recipe WHERE recipe.author.id = :userId", RecipeEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<RecipeEntity> listRecipesByIds(List<Long> recipeIds) {
        return list("id in ?1", recipeIds);
    }

    @Transactional
    public Long persistRecipeEntity(
            @Nullable RecipeEntity existingRecipeEntity,
            UserAccountEntity userAccountEntity,
            String title,
            @Nullable String description,
            Integer numServings,
            PreparationTime preparationTime,
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
        recipeEntity.numServings = numServings;
        recipeEntity.preparationTime = preparationTime;

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
