package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginAttemptRequest(
        @NotBlank @Size(max = 100) @Email String emailAddress, @NotBlank @Size(max = 200) String password) {}
