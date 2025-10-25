package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddUserFavoriteRecipeRequest(@NotNull @Positive Long recipeId) {}
