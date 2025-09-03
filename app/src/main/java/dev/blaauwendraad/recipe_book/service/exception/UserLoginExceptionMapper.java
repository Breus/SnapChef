package dev.blaauwendraad.recipe_book.service.exception;

import dev.blaauwendraad.recipe_book.resource.exception.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserLoginExceptionMapper implements ExceptionMapper<UserLoginException> {

    @Override
    public Response toResponse(UserLoginException exception) {
        var errorResponse = toErrorResponse(exception);
        return Response.status(errorResponse.httpStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResponse)
                .build();
    }

    private static ErrorResponse toErrorResponse(UserLoginException exception) {
        return new ErrorResponse(
                exception.getMessage() != null
                        ? exception.getMessage()
                        : "An unexpected error occurred while trying to log in",
                exception.getDetailMessage(),
                Response.Status.BAD_REQUEST.getStatusCode());
    }
}
