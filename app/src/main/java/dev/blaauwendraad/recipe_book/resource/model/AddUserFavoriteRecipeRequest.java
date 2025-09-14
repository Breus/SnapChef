package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddUserFavoriteRecipeRequest(@NotNull @Positive Long recipeId) {}
