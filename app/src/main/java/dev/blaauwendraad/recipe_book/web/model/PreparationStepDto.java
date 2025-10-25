package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PreparationStepDto(@NotBlank @Size(max = 400) String description) {}
