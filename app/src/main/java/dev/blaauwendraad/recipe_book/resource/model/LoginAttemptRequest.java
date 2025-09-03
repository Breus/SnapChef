package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LoginAttemptRequest(@Valid @NotNull LoginCredentialsDto loginCredentials) {}
