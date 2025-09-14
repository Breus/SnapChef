package dev.blaauwendraad.recipe_book.resource.model;

import java.util.List;

public record UserFavoriteRecipesResponse(List<Long> favoriteRecipesIds) {}
