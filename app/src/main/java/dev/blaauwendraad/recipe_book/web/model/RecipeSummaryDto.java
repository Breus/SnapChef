package dev.blaauwendraad.recipe_book.web.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.annotation.Nullable;

public record RecipeSummaryDto(
        Long id,
        String title,
        @Nullable String description,
        Boolean hasImage,
        Integer numServings,
        PreparationTime preparationTime,
        @Nullable RecipeAuthorDto author) {}
