package dev.blaauwendraad.recipe_book.resource.model;

import java.util.List;

public record RecipeDto(
        Integer id,
        String title,
        String description,
        String author,
        List<IngredientDto> ingredients,
        List<PreparationStepDto> steps) {}
