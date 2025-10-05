package dev.blaauwendraad.recipe_book.service.model;

import dev.blaauwendraad.recipe_book.model.Filter;
import java.util.Set;

public record UserAccount(
        Long id,
        String username,
        String emailAddress,
        String passwordHash,
        Filter defaultFilter,
        Set<UserRole> roles) {}
