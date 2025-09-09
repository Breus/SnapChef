package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SaveRecipeRequestDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotEmpty List<@Valid IngredientDto> ingredients,
        @NotEmpty List<@Valid PreparationStepDto> preparationSteps) {}
