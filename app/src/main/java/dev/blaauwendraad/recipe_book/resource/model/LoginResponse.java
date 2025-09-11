package dev.blaauwendraad.recipe_book.resource.model;

public record LoginResponse(Long userId, String username, String emailAddress, String authToken) {}
