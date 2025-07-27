package dev.blaauwendraad.recipe_book.resource.exception;

import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationException;
import dev.blaauwendraad.recipe_book.service.exception.UserRegistrationValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserRegistrationExceptionMapper implements ExceptionMapper<UserRegistrationException> {
    @Override
    public Response toResponse(UserRegistrationException exception) {
        var errorResponse = toErrorResponse(exception);
        return Response.status(errorResponse.httpStatusCode())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResponse)
                .build();
    }

    private static ErrorResponse toErrorResponse(UserRegistrationException exception) {
        var statusCode = exception instanceof UserRegistrationValidationException
                ? Response.Status.BAD_REQUEST.getStatusCode()
                : Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        var exceptionMessage =
                exception.getMessage() != null ? exception.getMessage() : "An unexpected error occurred.";
        return new ErrorResponse(exceptionMessage, exception.getDetailMessage(), statusCode);
    }
}
