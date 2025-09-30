package dev.blaauwendraad.recipe_book.resource.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
<<<<<<< HEAD
import jakarta.validation.constraints.NotNull;
=======
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
import jakarta.validation.constraints.Positive;
import java.util.List;

public record SaveRecipeRequestDto(
        @NotBlank String title,
        @NotBlank String description,
<<<<<<< HEAD
        @NotNull @Positive @Max(100) Integer numServings,
        @NotNull PreparationTime preparationTime,
=======
        @Positive Integer numServings,
        @Positive Integer preparationTime,
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
        @NotEmpty List<@Valid IngredientDto> ingredients,
        @NotEmpty List<@Valid PreparationStepDto> preparationSteps) {}
