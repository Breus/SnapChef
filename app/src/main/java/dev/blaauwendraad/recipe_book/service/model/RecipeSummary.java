package dev.blaauwendraad.recipe_book.service.model;

public record RecipeSummary(
        Long id,
        String title,
        String description,
        Author author
) {
}
