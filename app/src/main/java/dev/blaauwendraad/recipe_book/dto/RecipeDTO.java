package dev.blaauwendraad.recipe_book.dto;

import java.util.List;

public record RecipeDTO(Integer id, String title, String description, String author, List<IngredientDTO> ingredients,
                        List<PreparationStepDTO> steps) {
}