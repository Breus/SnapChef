package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.resource.model.AddUserFavoriteRecipeRequest;
import dev.blaauwendraad.recipe_book.resource.model.LoginAttemptRequest;
import dev.blaauwendraad.recipe_book.resource.model.LoginResponse;
import dev.blaauwendraad.recipe_book.resource.model.UserFavoriteRecipesIdsResponse;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationRequest;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationResponse;
import dev.blaauwendraad.recipe_book.service.UserAuthenticationService;
import dev.blaauwendraad.recipe_book.service.UserFavoriteRecipesService;
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
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/users")
@ApplicationScoped
public class UserResource {
    private final UserFavoriteRecipesService userFavoriteRecipesService;
    private final UserAuthenticationService userAuthenticationService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public UserResource(
            UserFavoriteRecipesService userFavoriteRecipesService,
            UserAuthenticationService userAuthenticationService) {
        this.userFavoriteRecipesService = userFavoriteRecipesService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Path("/{userId}/recipes/favorites")
    public UserFavoriteRecipesIdsResponse getUserFavoriteRecipesIds(@PathParam("userId") Long userId) {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to request favorite recipes of other user");
        }
        return new UserFavoriteRecipesIdsResponse(userFavoriteRecipesService.getUserFavoriteRecipes(userId));
    }

    @POST
    @RolesAllowed({"admin", "user"})
    @Path("/{userId}/recipes/favorites")
    public UserFavoriteRecipesIdsResponse addUserFavoriteRecipe(
            @PathParam("userId") Long userId, @Valid @NotNull AddUserFavoriteRecipeRequest request) {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to favorite recipes for others users");
        }
        return new UserFavoriteRecipesIdsResponse(
                userFavoriteRecipesService.addUserFavoriteRecipe(userId, request.recipeId()));
    }

    @DELETE
    @RolesAllowed({"admin", "user"})
    @Path("/{userId}/recipes/favorites/{recipeId}")
    public UserFavoriteRecipesIdsResponse removeUserFavoriteRecipe(
            @PathParam("userId") Long userId, @PathParam("recipeId") Long recipeId) {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to favorite recipes for others users");
        }
        return new UserFavoriteRecipesIdsResponse(
                userFavoriteRecipesService.removeUserFavoriteRecipe(userId, recipeId));
    }

    @POST
    @PermitAll
    @Path("/login")
    public LoginResponse login(@Valid @NotNull LoginAttemptRequest loginAttemptRequest) throws UserLoginException {
        AuthenticationDetails loginDetails = userAuthenticationService.login(
                loginAttemptRequest.loginCredentials().emailAddress(),
                loginAttemptRequest.loginCredentials().password());
        return new LoginResponse(
                loginDetails.userId(),
                loginDetails.username(),
                loginDetails.emailAddress(),
                loginDetails.accessToken(),
                loginDetails.refreshToken(),
                loginDetails.refreshExpiresInSeconds());
    }

    @POST
    @PermitAll
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
