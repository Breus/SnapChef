package dev.blaauwendraad.recipe_book.service.model;

public record AuthenticationDetails(
        Long userId,
        String username,
        String emailAddress,
        String accessToken,
        String refreshToken,
        Long refreshExpiresInSeconds) {}
