package dev.blaauwendraad.recipe_book.resource.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Email String emailAddress,
        @NotBlank
                @Size(min = 8, max = 100)
                @Pattern(
                        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                        message =
                                "Password must contain at least one uppercase letter, one lowercase letter, and one number")
                String password) {}
