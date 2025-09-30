package dev.blaauwendraad.recipe_book.resource.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.annotation.Nullable;
import java.util.List;

public record RecipeDto(
        Long id,
        String title,
        @Nullable String description,
        Integer numServings,
<<<<<<< HEAD
        PreparationTime preparationTime,
=======
        @Nullable Integer preparationTime,
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
        @Nullable RecipeAuthorDto author,
        List<IngredientDto> ingredients,
        List<PreparationStepDto> preparationSteps) {}
