package dev.blaauwendraad.recipe_book.resource.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.annotation.Nullable;
import java.util.List;

public record RecipeDto(
        Long id,
        String title,
        @Nullable String description,
        Integer numServings,
        PreparationTime preparationTime,
        @Nullable RecipeAuthorDto author,
        List<IngredientDto> ingredients,
        List<PreparationStepDto> preparationSteps) {}
