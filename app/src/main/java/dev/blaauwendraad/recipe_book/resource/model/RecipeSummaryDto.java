package dev.blaauwendraad.recipe_book.resource.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.annotation.Nullable;

public record RecipeSummaryDto(
        Long id,
        String title,
        @Nullable String description,
        Integer numServings,
<<<<<<< HEAD
        PreparationTime preparationTime,
=======
        @Nullable Integer preparationTime,
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
        @Nullable RecipeAuthorDto author) {}
