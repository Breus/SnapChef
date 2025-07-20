package dev.blaauwendraad.recipe_book.resource.model;

public record RecipeSummaryDto(Long id, String title, String description, RecipeAuthorDto author) {}
