package dev.blaauwendraad.recipe_book.resource.model;

import java.util.Set;

public record UserFavoriteRecipesResponse(Set<Long> favoriteRecipesIds) {}
