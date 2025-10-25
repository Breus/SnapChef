package dev.blaauwendraad.recipe_book.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(max = 100) @Email String emailAddress,
        @NotBlank
                @Size(min = 8, max = 200)
                @Pattern(
                        regexp = "^(?=.*[a-z])(?=.*[A-Z]).+$",
                        message = "Password must contain at least one uppercase letter, one lowercase letter")
                String password) {}
