package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(@NotBlank @Size(max = 256) String refreshToken) {}
