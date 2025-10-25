package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientDto(@NotBlank @Size(max = 100) String name, @NotBlank @Size(max = 50) String quantity) {}
