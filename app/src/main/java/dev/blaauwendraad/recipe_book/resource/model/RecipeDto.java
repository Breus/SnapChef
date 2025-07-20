package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.annotation.Nullable;
import java.util.List;

public record RecipeDto(
        Integer id,
        String title,
        @Nullable String description,
        @Nullable String author,
        List<IngredientDto> ingredients,
        List<PreparationStepDto> steps) {}
