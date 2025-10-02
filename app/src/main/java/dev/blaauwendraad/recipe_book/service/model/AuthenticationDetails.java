package dev.blaauwendraad.recipe_book.service.model;

public record AuthenticationDetails(
        Long userId,
        String username,
        String emailAddress,
        String accessToken,
        Long expiresInSeconds,
        String refreshToken,
        Long refreshExpiresInSeconds) {}
