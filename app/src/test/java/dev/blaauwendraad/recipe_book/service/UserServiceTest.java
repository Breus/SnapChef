package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
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
        var userAccountEntity = new UserAccountEntity();
        userAccountEntity.id = 1L;
        userAccountEntity.username = duplicateUserName;
        userAccountEntity.emailAddress = "test@example.com";
        userAccountEntity.passwordHash = "fakehash";
        userAccountEntity.roles = Set.of();

        Mockito.when(userRepository.findByUsername(duplicateUserName))
                .thenReturn(null) // first registration: username not taken
                .thenReturn(userAccountEntity);

        Mockito.when(userRepository.createUser(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anySet()))
                .thenReturn(userAccountEntity);
        // When, Then
        // First registration of this username should succeed
        Assertions.assertThatNoException()
                .isThrownBy(
                        () -> userService.registerUser(duplicateUserName, "some-mail@example.com", "Fakepassword123"));
        // Second registration of the same username should throw an exception
        Assertions.assertThatThrownBy(
                        () -> userService.registerUser(duplicateUserName, "different@example.com", "someFakePassword"))
                .isInstanceOf(UserRegistrationValidationException.class)
                .hasMessageContaining("Failed to register new user.")
                .hasFieldOrPropertyWithValue("detailMessage", "Invalid user provided: Username is already in use.");
    }
}
