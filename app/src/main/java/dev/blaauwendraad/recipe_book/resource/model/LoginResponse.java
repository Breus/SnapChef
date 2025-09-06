package dev.blaauwendraad.recipe_book.resource.model;

public record LoginResponse(String userId, String username, String emailAddress, String authToken) {}
