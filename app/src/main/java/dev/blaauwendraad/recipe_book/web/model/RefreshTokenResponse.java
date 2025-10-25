package dev.blaauwendraad.recipe_book.web.model;

public record RefreshTokenResponse(String accessToken, Long expiresInSeconds) {}
