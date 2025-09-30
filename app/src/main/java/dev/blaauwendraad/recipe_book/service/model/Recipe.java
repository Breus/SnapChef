package dev.blaauwendraad.recipe_book.service.model;

import jakarta.annotation.Nullable;
import java.util.List;

public record Recipe(
        Long id,
        String title,
        @Nullable String description,
        Integer numServings,
<<<<<<< HEAD
        PreparationTime preparationTime,
=======
        @Nullable Integer preparationTime,
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
        @Nullable Author author,
        List<Ingredient> ingredients,
        List<PreparationStep> preparationSteps) {}
