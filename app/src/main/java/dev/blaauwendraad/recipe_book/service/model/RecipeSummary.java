package dev.blaauwendraad.recipe_book.service.model;

import jakarta.annotation.Nullable;

public record RecipeSummary(Long id, String title, @Nullable String description, @Nullable Author author) {}
