package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.constraints.NotBlank;

public record IngredientDto(@NotBlank String name, @NotBlank String quantity) {}
