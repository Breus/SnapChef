package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateEmailRequest(@NotNull @Email String newEmail, @NotNull String currentPassword) {}
