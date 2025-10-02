package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.resource.model.AddUserFavoriteRecipeRequest;
import dev.blaauwendraad.recipe_book.resource.model.UserFavoriteRecipesIdsResponse;
import dev.blaauwendraad.recipe_book.service.UserFavoriteRecipesService;
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
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@Path("/users/{userId}/recipes/favorites")
public class UserFavoriteRecipesResource {
    private final UserFavoriteRecipesService userFavoriteRecipesService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public UserFavoriteRecipesResource(UserFavoriteRecipesService userFavoriteRecipesService) {
        this.userFavoriteRecipesService = userFavoriteRecipesService;
    }

    @GET
    @RolesAllowed({"admin", "user"})
    public UserFavoriteRecipesIdsResponse getUserFavoriteRecipesIds(@PathParam("userId") Long userId) {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to request favorite recipes of other user");
        }
        return new UserFavoriteRecipesIdsResponse(userFavoriteRecipesService.getUserFavoriteRecipes(userId));
    }

    @POST
    @RolesAllowed({"admin", "user"})
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
    @Path("/{recipeId}")
    public UserFavoriteRecipesIdsResponse removeUserFavoriteRecipe(
            @PathParam("userId") Long userId, @PathParam("recipeId") Long recipeId) {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to favorite recipes for others users");
        }
        return new UserFavoriteRecipesIdsResponse(
                userFavoriteRecipesService.removeUserFavoriteRecipe(userId, recipeId));
    }
}
