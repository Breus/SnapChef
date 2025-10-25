package dev.blaauwendraad.recipe_book.web;

import dev.blaauwendraad.recipe_book.service.UserAuthenticationService;
import dev.blaauwendraad.recipe_book.service.exception.AccessTokenRefreshException;
import dev.blaauwendraad.recipe_book.service.exception.UserLoginException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.model.AuthenticationDetails;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.web.model.LoginAttemptRequest;
import dev.blaauwendraad.recipe_book.web.model.LoginResponse;
import dev.blaauwendraad.recipe_book.web.model.RefreshTokenRequest;
import dev.blaauwendraad.recipe_book.web.model.RefreshTokenResponse;
import dev.blaauwendraad.recipe_book.web.model.UserRegistrationRequest;
import dev.blaauwendraad.recipe_book.web.model.UserRegistrationResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@ApplicationScoped
@Path("/api/users/authn/")
public class UserAuthenticationResource {
    private final UserAuthenticationService userAuthenticationService;

    @Inject
    public UserAuthenticationResource(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @POST
    @PermitAll
    @Path("/login")
    public LoginResponse login(@Valid @NotNull LoginAttemptRequest loginAttemptRequest) throws UserLoginException {
        AuthenticationDetails loginDetails =
                userAuthenticationService.login(loginAttemptRequest.emailAddress(), loginAttemptRequest.password());
        return new LoginResponse(
                loginDetails.userId(),
                loginDetails.username(),
                loginDetails.emailAddress(),
                loginDetails.accessToken(),
                loginDetails.expiresInSeconds(),
                loginDetails.refreshToken(),
                loginDetails.refreshExpiresInSeconds());
    }

    @POST
    @PermitAll
    @Path("/refresh")
    public RefreshTokenResponse refreshToken(@Valid @NotNull RefreshTokenRequest refreshTokenRequest)
            throws AccessTokenRefreshException {
        return userAuthenticationService.refreshAccessToken(refreshTokenRequest.refreshToken());
    }

    @POST
    @PermitAll
    @Path("/register")
    public Response register(@Valid @NotNull UserRegistrationRequest registrationRequest)
            throws UserRegistrationException {
        UserAccount userAccount = userAuthenticationService.registerUser(
                registrationRequest.username(), registrationRequest.emailAddress(), registrationRequest.password());
        return Response.created(URI.create("/users/" + userAccount.id()))
                .entity(new UserRegistrationResponse(
                        userAccount.id(), userAccount.username(), userAccount.emailAddress()))
                .build();
    }
}
