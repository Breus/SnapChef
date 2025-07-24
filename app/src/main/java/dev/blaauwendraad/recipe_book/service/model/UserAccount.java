package dev.blaauwendraad.recipe_book.service.model;

import java.util.Set;

public record UserAccount(Long id, String username, String emailAddress, String passwordHash, Set<UserRole> roles) {}
