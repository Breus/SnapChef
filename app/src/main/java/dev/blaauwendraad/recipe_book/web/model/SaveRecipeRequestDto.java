package dev.blaauwendraad.recipe_book.web.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SaveRecipeRequestDto(
        @NotBlank @Size(min = 5, max = 100) String title,
        @Nullable @Size(max = 2000) String description,
        @Nullable @Size(max = 255) String imageName,
        @NotNull @Positive @Max(100) Integer numServings,
        @NotNull PreparationTime preparationTime,
        @Size(min = 1, max = 50) List<@Valid IngredientDto> ingredients,
        @Size(min = 1, max = 50) List<@Valid PreparationStepDto> preparationSteps) {}
