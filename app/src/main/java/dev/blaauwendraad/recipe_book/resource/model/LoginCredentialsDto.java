package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginCredentialsDto(@NotBlank @Email String emailAddress, @NotBlank String password) {}
