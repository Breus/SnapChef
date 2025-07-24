package dev.blaauwendraad.recipe_book.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import dev.blaauwendraad.recipe_book.data.model.RoleEntity;
import dev.blaauwendraad.recipe_book.data.model.RoleName;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.resource.model.UserRegistrationRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class UserResourceTest {

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
                .post("/users/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("username", is("testuser"))
                .body("emailAddress", is("test@example.com"));
    }

    // First fix error handling in the JAX-RS application to be able to test for errors
    //    @Test
    //    void registerUser_DuplicateUsername_Returns400() {
    //        UserRegistrationRequest request = new UserRegistrationRequest(
    //                "testuser",
    //                "test@example.com",
    //                "Password123!"
    //        );
    //
    //        // Register first user
    //        given()
    //                .contentType(ContentType.JSON)
    //                .body(request)
    //                .post("/users/register");
    //
    //        // Try to register second user with same username
    //        UserRegistrationRequest duplicateRequest = new UserRegistrationRequest(
    //                "testuser",
    //                "different@example.com",
    //                "Password123!"
    //        );
    //
    //        given()
    //                .contentType(ContentType.JSON)
    //                .body(duplicateRequest)
    //                .when()
    //                .post("/users/register")
    //                .then()
    //                .statusCode(400)
    //                .body("message", is("Username is already in use."));
    //    }
}
