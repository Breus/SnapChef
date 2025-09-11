package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.resource.model.LoginAttemptRequest;
import dev.blaauwendraad.recipe_book.resource.model.LoginResponse;
import dev.blaauwendraad.recipe_book.resource.model.UserFavoriteRecipesResponse;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationRequest;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationResponse;
import dev.blaauwendraad.recipe_book.service.UserService;
import dev.blaauwendraad.recipe_book.service.exception.UserLoginException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.model.AuthenticationDetails;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/users")
@ApplicationScoped
public class UserResource {
    private final UserService userService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Path("/{userId}/recipes/favorites")
    public UserFavoriteRecipesResponse getUserFavoriteRecipes(@PathParam("userId") Long userId) {
        if (!Long.valueOf(jwt.getClaim("upn")).equals(userId)) {
            throw new NotAllowedException("Not allowed to request favorite recipes of other user");
        }
        return new UserFavoriteRecipesResponse(userService.getUserFavoriteRecipes(userId));
    }

    @POST
    @PermitAll
    @Path("/login")
    public LoginResponse login(@Valid @NotNull LoginAttemptRequest loginAttemptRequest) throws UserLoginException {
        AuthenticationDetails loginDetails = userService.login(
                loginAttemptRequest.loginCredentials().emailAddress(),
                loginAttemptRequest.loginCredentials().password());
        return new LoginResponse(
                loginDetails.userId(), loginDetails.username(), loginDetails.emailAddress(), loginDetails.authToken());
    }

    @POST
    @PermitAll
    public Response register(@Valid @NotNull UserRegistrationRequest registrationRequest)
            throws UserRegistrationException {
        UserAccount userAccount = userService.registerUser(
                registrationRequest.username(), registrationRequest.emailAddress(), registrationRequest.password());
        return Response.created(URI.create("/users/" + userAccount.id()))
                .entity(new UserRegistrationResponse(
                        userAccount.id(), userAccount.username(), userAccount.emailAddress()))
                .build();
    }
}
