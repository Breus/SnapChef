package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.RefreshTokenEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.RefreshTokenRepository;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserLoginException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import dev.blaauwendraad.recipe_book.service.model.AuthenticationDetails;
import io.quarkus.elytron.security.common.BcryptUtil;
import java.time.Instant;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationTest {
    private static final String TEST_PASSWORD = "Password123";
    private UserRepository userRepository;
    private UserAuthenticationService userAuthenticationService;
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeAll
    static void setup() {
        Assertions.setMaxStackTraceElementsDisplayed(500); // or any number you want
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        userAuthenticationService = new UserAuthenticationService(userRepository, refreshTokenRepository);
    }

    @Test
    void cannotRegisterUserWithExistingUsername() {
        // Given
        var duplicateUserName = "testuser";
        var userAccountEntity = createUserAccountEntity(duplicateUserName, "someone@example.com");

        Mockito.when(userRepository.findByUsername(duplicateUserName))
                .thenReturn(null) // first registration: userName not taken
                .thenReturn(userAccountEntity);

        Mockito.when(userRepository.createUser(
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anySet()))
                .thenReturn(userAccountEntity);
        // When, Then
        // The first registration of this userName should succeed
        Assertions.assertThatNoException()
                .isThrownBy(() -> userAuthenticationService.registerUser(
                        duplicateUserName, "some-mail@example.com", TEST_PASSWORD));
        // The second registration of the same userName should throw an exception
        Assertions.assertThatThrownBy(() -> userAuthenticationService.registerUser(
                        duplicateUserName, "different@example.com", TEST_PASSWORD))
                .isInstanceOf(UserRegistrationValidationException.class)
                .hasMessageContaining("Failed to register new user.")
                .hasFieldOrPropertyWithValue("detailMessage", "Invalid user provided: Username is already in use.");
    }

    @Test
    void login() throws UserLoginException {
        // Given
        var USER_EMAIL = "breus-test@example.com";
        var REFRESH_TOKEN = "refresh-test-token";

        UserAccountEntity userAccountEntity = createUserAccountEntity("breus-test", USER_EMAIL);
        Mockito.when(userRepository.findByEmail(USER_EMAIL)).thenReturn(userAccountEntity);
        Mockito.when(userRepository.findById(1L)).thenReturn(userAccountEntity);

        RefreshTokenEntity refreshTokenEntity = createRefreshTokenEntity(userAccountEntity, REFRESH_TOKEN);
        Mockito.when(refreshTokenRepository.addRefreshToken(
                        Mockito.eq(userAccountEntity), Mockito.any(), Mockito.any()))
                .thenReturn(refreshTokenEntity);

        // When
        AuthenticationDetails authenticationDetails = userAuthenticationService.login(USER_EMAIL, TEST_PASSWORD);

        // Then
        Assertions.assertThat(authenticationDetails.userId()).isEqualTo(1L);
        Assertions.assertThat(authenticationDetails.username()).isEqualTo("breus-test");
        Assertions.assertThat(authenticationDetails.emailAddress()).isEqualTo(USER_EMAIL);
        Assertions.assertThat(authenticationDetails.authToken()).isNotBlank();
        Assertions.assertThat(authenticationDetails.refreshToken()).isEqualTo(REFRESH_TOKEN);
    }

    private RefreshTokenEntity createRefreshTokenEntity(UserAccountEntity userAccountEntity, String token) {
        var refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.userId = userAccountEntity.id;
        refreshTokenEntity.token = token;
        refreshTokenEntity.expiresAt = Instant.now().plusSeconds(3600);
        return refreshTokenEntity;
    }

    private UserAccountEntity createUserAccountEntity(String username, String emailAddress) {
        var userAccountEntity = new UserAccountEntity();
        userAccountEntity.id = 1L;
        userAccountEntity.username = username;
        userAccountEntity.emailAddress = emailAddress;
        userAccountEntity.passwordHash = BcryptUtil.bcryptHash(TEST_PASSWORD);
        userAccountEntity.roles = Set.of();
        return userAccountEntity;
    }
}
