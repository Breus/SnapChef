package dev.blaauwendraad.recipe_book.web.model;

public record LoginResponse(
        Long userId,
        String username,
        String emailAddress,
        String accessToken,
        Long expiresInSeconds,
        String refreshToken,
        Long refreshExpiresInSeconds) {}
