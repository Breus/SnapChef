package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    public void registerUser(String username, String emailAddress, String password) throws UserRegistrationException {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
