package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationRequest;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationResponse;
import dev.blaauwendraad.recipe_book.service.UserService;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/users")
@ApplicationScoped
public class UserResource {
    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/register")
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
