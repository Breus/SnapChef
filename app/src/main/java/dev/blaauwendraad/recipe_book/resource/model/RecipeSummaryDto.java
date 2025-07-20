package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.annotation.Nullable;

public record RecipeSummaryDto(Long id, String title, @Nullable String description, @Nullable RecipeAuthorDto author) {}
