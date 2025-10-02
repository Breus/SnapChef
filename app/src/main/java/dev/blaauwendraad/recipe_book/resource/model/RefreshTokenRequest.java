package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {}
