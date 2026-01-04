package dev.blaauwendraad.recipe_book.web;

import dev.blaauwendraad.recipe_book.service.UserService;
import dev.blaauwendraad.recipe_book.service.exception.UserAuthenticationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import dev.blaauwendraad.recipe_book.web.model.UpdateEmailRequest;
import dev.blaauwendraad.recipe_book.web.model.UpdatePasswordRequest;
import dev.blaauwendraad.recipe_book.web.model.UserAccountDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@Path("/api/users/{userId}")
public class UserResource {
    private final UserService userService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public UserAccountDto getUser(@PathParam("userId") Long userId) {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to get other user");
        }
        UserAccount user = userService.getUser(userId);
        return new UserAccountDto(user.id(), user.username(), user.emailAddress());
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/email")
    @RolesAllowed({"admin", "user"})
    public Response updateEmail(@PathParam("userId") Long userId, @Valid @NotNull UpdateEmailRequest request)
            throws UserAuthenticationException {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to update e-mail of other user");
        }
        userService.updateEmail(userId, request.newEmail(), request.currentPassword());
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/password")
    @RolesAllowed({"admin", "user"})
    public Response updatePassword(@PathParam("userId") Long userId, @Valid @NotNull UpdatePasswordRequest request)
            throws UserAuthenticationException {
        if (!Long.valueOf(jwt.getName()).equals(userId)) {
            throw new ForbiddenException("Not allowed to update password of other user");
        }
        userService.updatePassword(userId, request.currentPassword(), request.newPassword());
        return Response.ok().build();
    }
}
