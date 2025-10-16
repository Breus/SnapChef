package dev.blaauwendraad.recipe_book.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import dev.blaauwendraad.recipe_book.data.model.RoleEntity;
import dev.blaauwendraad.recipe_book.data.model.RoleName;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAuthenticationResourceTest {

    @BeforeEach
    @Transactional
    void setUp() {
        UserAccountEntity.deleteAll();
        RoleEntity.deleteAll();

        RoleEntity userRole = new RoleEntity();
        userRole.roleName = RoleName.user;
        userRole.persist();

        RoleEntity adminRole = new RoleEntity();
        adminRole.roleName = RoleName.admin;
        adminRole.persist();
    }

    @Test
    void registerUser_ValidInput_Returns200() {
        UserRegistrationRequest request = new UserRegistrationRequest("testuser", "test@example.com", "Password123!");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .when()
                .post("/api/users/authn/register")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("id", notNullValue())
                .body("username", is("testuser"))
                .body("emailAddress", is("test@example.com"));
    }

    @Test
    void registerUser_DuplicateUsername_Returns400() {
        UserRegistrationRequest request = new UserRegistrationRequest("testuser", "test@example.com", "Password123!");

        // Register the first user successfully
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/users/authn/register")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        // Try to register second user with same username
        UserRegistrationRequest duplicateRequest =
                new UserRegistrationRequest("testuser", "different@example.com", "Password123!");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(duplicateRequest)
                .when()
                .post("/api/users/authn/register")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("title", is("Failed to register new user."))
                .body("detail", is("Username is already in use."))
                .body("status", is(Response.Status.BAD_REQUEST.getStatusCode()));
    }
}
