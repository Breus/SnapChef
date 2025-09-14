package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.repository.UserRepository;
import dev.blaauwendraad.recipe_book.service.exception.UserLoginException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import dev.blaauwendraad.recipe_book.service.model.AuthenticationDetails;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.service.model.UserRole;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {
    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return toUserAccount(userRepository.createUser(username, hashPassword, emailAddress, Set.of(UserRole.user)));
    }

    /**
     * Logs in a user with the provided e-mail address and password and returns an authentication authToken.
     *
     * @param emailAddress the e-mail address of the user
     * @param password     the password of the user
     * @throws UserLoginException if there is an error during user login
     */
    public AuthenticationDetails login(String emailAddress, String password) throws UserLoginException {
        UserAccount userAccount = validateCredentials(emailAddress, password);
        return new AuthenticationDetails(
                userAccount.id(),
                userAccount.username(),
                userAccount.emailAddress(),
                Jwt.issuer("https://snapchef.blaauwendraad.dev")
                        .upn(userAccount.id().toString())
                        .claim("email", userAccount.emailAddress())
                        .claim("userName", userAccount.username())
                        .groups(userAccount.roles().stream().map(Enum::name).collect(Collectors.toSet()))
                        .sign());
    }

    private UserAccount validateCredentials(String emailAddress, String password) throws UserLoginException {
        UserAccountEntity userAccountEntity = userRepository.findByEmail(emailAddress);
        if (userAccountEntity == null) {
            throw new UserLoginException("No user account found for the provided email address.");
        }
        if (!BcryptUtil.matches(password, userAccountEntity.passwordHash)) {
            throw new UserLoginException("Invalid password provided.");
        }
        return toUserAccount(userAccountEntity);
    }

    private static UserAccount toUserAccount(UserAccountEntity userAccountEntity) {
        return new UserAccount(
                userAccountEntity.id,
                userAccountEntity.username,
                userAccountEntity.emailAddress,
                userAccountEntity.passwordHash,
                userAccountEntity.roles.stream()
                        .map(role -> switch (role.roleName) {
                            case admin -> UserRole.admin;
                            case user -> UserRole.user;
                        })
                        .collect(Collectors.toSet()));
    }

    public List<Long> getUserFavoriteRecipes(Long userId) {
        return userRepository.findById(userId).favoriteRecipes.stream()
                .map(r -> r.id)
                .toList();
    }

    public List<Long> addUserFavoriteRecipe(Long userId, Long recipeId) {
        return userRepository.addFavoriteRecipe(userId, recipeId).favoriteRecipes.stream()
                .map(r -> r.id)
                .toList();
    }

    public List<Long> removeUserFavoriteRecipe(Long userId, Long recipeId) {
        return userRepository.removeFavoriteRecipe(userId, recipeId).favoriteRecipes.stream()
                .map(r -> r.id)
                .toList();
    }
}
