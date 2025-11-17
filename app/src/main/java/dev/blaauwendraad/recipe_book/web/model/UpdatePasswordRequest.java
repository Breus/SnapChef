package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(@NotNull String currentPassword, @NotNull @Size(min = 8) String newPassword) {}
