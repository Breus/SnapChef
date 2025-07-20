package dev.blaauwendraad.recipe_book.service.model;

import java.util.List;

public record Recipe(
        Long id,
        String title,
        String description,
        Author author,
        List<Ingredient> ingredients,
        List<PreparationStep> preparationSteps) {}
