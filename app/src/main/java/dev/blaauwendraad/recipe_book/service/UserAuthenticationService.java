package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.RefreshTokenEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.RefreshTokenRepository;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.AccessTokenRefreshException;
import dev.blaauwendraad.recipe_book.service.exception.UserLoginException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import dev.blaauwendraad.recipe_book.service.model.AuthenticationDetails;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserAuthenticationService {
    private static final Duration AUTH_TOKEN_EXPIRY_DURATION = Duration.ofMinutes(15);
    private static final Duration REFRESH_TOKEN_EXPIRY_DURATION = Duration.ofDays(30);
    private static final Integer REFRESH_TOKEN_BYTE_SIZE = 64;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserAuthenticationService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Registers a new user with the provided authorName, email address, and password.
     *
     * @param username     the authorName of the new user
     * @param emailAddress the email address of the new user
     * @param password     the password for the new user
     * @throws UserRegistrationException if there is an error during registration
     */
    public UserAccount registerUser(String username, String emailAddress, String password)
            throws UserRegistrationException {
        if (userRepository.findByEmail(emailAddress) != null) {
            throw new UserRegistrationValidationException("Email address is already in use.");
        }
        if (userRepository.findByUsername(username) != null) {
            throw new UserRegistrationValidationException("Username is already in use.");
        }
        String hashPassword = BcryptUtil.bcryptHash(password);
        return UserAccountConverter.toUserAccount(
                userRepository.createUser(username, hashPassword, emailAddress, Set.of(UserRole.user)));
    }

    /**
     * Logs in a user with the provided e-mail address and password and returns an authentication (JWT) token.
     *
     * @param emailAddress the e-mail address of the user
     * @param password     the password of the user
     * @throws UserLoginException if there is an error during user login
     */
    public AuthenticationDetails login(String emailAddress, String password) throws UserLoginException {
        UserAccount userAccount = validateCredentials(emailAddress, password);
        RefreshTokenEntity refreshTokenEntity = createAndStoreRefreshToken(userAccount);
        return new AuthenticationDetails(
                userAccount.id(),
                userAccount.username(),
                userAccount.emailAddress(),
                createAccessToken(userAccount),
                refreshTokenEntity.token,
                Duration.between(Instant.now(), refreshTokenEntity.expiresAt).toSeconds());
    }

    /**
     * Creates a new access token using the provided refresh token.
     * @param refreshToken the refresh token to use for token creation
     * @return the new access token (JWT)
     * @throws AccessTokenRefreshException if there is an error during token creation
     */
    public String refreshAccessToken(String refreshToken) throws AccessTokenRefreshException {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null) {
            throw new AccessTokenRefreshException("Unknown refresh token provided.");
        }
        if (!refreshTokenEntity.valid) {
            throw new AccessTokenRefreshException("Invalid refresh token provided.");
        }
        if (refreshTokenEntity.expiresAt.isBefore(Instant.now())) {
            throw new AccessTokenRefreshException("Refresh token has expired.");
        }
        var userAccount = UserAccountConverter.toUserAccount(refreshTokenEntity.user);
        return createAccessToken(userAccount);
    }

    private String createAccessToken(UserAccount userAccount) {
        return Jwt.issuer("https://snapchef.blaauwendraad.dev")
                .upn(userAccount.id().toString())
                .claim("email", userAccount.emailAddress())
                .claim("username", userAccount.username())
                .groups(userAccount.roles().stream().map(Enum::name).collect(Collectors.toSet()))
                .expiresIn(AUTH_TOKEN_EXPIRY_DURATION)
                .sign();
    }

    private RefreshTokenEntity createAndStoreRefreshToken(UserAccount userAccount) {
        var userAccountEntity = userRepository.findById(userAccount.id());
        var refreshToken = generateRefreshToken();
        var expiryInstant = Instant.now().plus(REFRESH_TOKEN_EXPIRY_DURATION);
        return refreshTokenRepository.addRefreshToken(userAccountEntity, refreshToken, expiryInstant);
    }

    private String generateRefreshToken() {
        var secureRandom = new SecureRandom();
        var bytes = new byte[REFRESH_TOKEN_BYTE_SIZE];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private UserAccount validateCredentials(String emailAddress, String password) throws UserLoginException {
        UserAccountEntity userAccountEntity = userRepository.findByEmail(emailAddress);
        if (userAccountEntity == null) {
            throw new UserLoginException("No user account found for the provided email address.");
        }
        if (!BcryptUtil.matches(password, userAccountEntity.passwordHash)) {
            throw new UserLoginException("Invalid password provided.");
        }
        return UserAccountConverter.toUserAccount(userAccountEntity);
    }
}
