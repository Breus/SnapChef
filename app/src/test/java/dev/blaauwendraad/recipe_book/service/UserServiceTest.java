package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeAll
    static void setup() {
        Assertions.setMaxStackTraceElementsDisplayed(500); // or any number you want
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void cannotRegisterUserWithExistingUsername() {
        // Given
        var duplicateUserName = "testuser";
        Mockito.when(userRepository.findByUsername(duplicateUserName))
                .thenReturn(null) // first registration: username not taken
                .thenReturn(new UserAccount(1L, duplicateUserName, "test@example.com", "fakehash", Set.of()));

        // When, Then
        // First registration of this username should succeed
        Assertions.assertThatNoException()
                .isThrownBy(() -> userService.registerUser(duplicateUserName, "password123", "some-mail@example.com"));
        // Second registration of the same username should throw an exception
        Assertions.assertThatThrownBy(
                        () -> userService.registerUser(duplicateUserName, "differentpassword", "different@example.com"))
                .isInstanceOf(UserRegistrationValidationException.class)
                .hasMessageContaining("Failed to register new user.")
                .hasFieldOrPropertyWithValue("detailMessage", "Invalid user provided: Username is already in use.");
    }
}
