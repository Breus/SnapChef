package dev.blaauwendraad.recipe_book.service.model;

import jakarta.annotation.Nullable;
import java.util.List;

public record Recipe(
        Long id,
        String title,
        @Nullable String description,
        Integer numServings,
        PreparationTime preparationTime,
        @Nullable Author author,
        List<Ingredient> ingredients,
        List<PreparationStep> preparationSteps) {}
