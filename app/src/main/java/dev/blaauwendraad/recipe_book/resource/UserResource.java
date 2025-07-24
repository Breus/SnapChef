package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationRequest;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationResponse;
import dev.blaauwendraad.recipe_book.service.UserService;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import dev.blaauwendraad.recipe_book.service.model.UserAccount;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

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
    public UserRegistrationResponse register(@Valid @NotNull UserRegistrationRequest registrationRequest) {
        try {
            UserAccount userAccount = userService.registerUser(
                    registrationRequest.username(), registrationRequest.emailAddress(), registrationRequest.password());
            return new UserRegistrationResponse(userAccount.id(), userAccount.username(), userAccount.emailAddress());
        } catch (UserRegistrationException e) {
            if (e instanceof UserRegistrationValidationException) {
                throw new BadRequestException(e.getMessage(), e);
            }
            throw new InternalServerErrorException("An unexpected error occurred during user registration.", e);
        }
    }
}
