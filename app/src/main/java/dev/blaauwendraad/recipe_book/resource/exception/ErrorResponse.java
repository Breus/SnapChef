package dev.blaauwendraad.recipe_book.resource.exception;

/*
 * This ErrorResponse class is used to represent error responses in the HTTP API and is inspired by RFC-7807 but
 * doesn't have all the fields specified in that RFC.
 */
import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(String title, String detail, @JsonProperty("status") int httpStatusCode) {}
