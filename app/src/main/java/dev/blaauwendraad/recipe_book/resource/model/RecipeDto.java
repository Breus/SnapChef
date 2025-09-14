package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.annotation.Nullable;
import java.util.List;

public record RecipeDto(
        Long id,
        String title,
        @Nullable String description,
        @Nullable RecipeAuthorDto author,
        List<IngredientDto> ingredients,
        List<PreparationStepDto> preparationSteps) {}
