package dev.blaauwendraad.recipe_book.resource.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record SaveRecipeRequestDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull @Positive @Max(100) Integer numServings,
        @NotNull PreparationTime preparationTime,
        @NotEmpty List<@Valid IngredientDto> ingredients,
        @NotEmpty List<@Valid PreparationStepDto> preparationSteps) {}
