package dev.blaauwendraad.recipe_book.resource.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SaveRecipeRequestDto(
        @NotBlank @Size(min = 5, max = 100) String title,
        @NotBlank @Size(max = 2000) String description,
        @NotNull @Positive @Max(100) Integer numServings,
        @NotNull PreparationTime preparationTime,
        @Size(min = 1, max = 50) List<@Valid IngredientDto> ingredients,
        @Size(min = 1, max = 50) List<@Valid PreparationStepDto> preparationSteps) {}
