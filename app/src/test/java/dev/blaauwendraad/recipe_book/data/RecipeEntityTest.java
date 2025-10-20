package dev.blaauwendraad.recipe_book.data;

import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import dev.blaauwendraad.recipe_book.test.PostgresResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
class RecipeEntityTest {

    @Test
    @Transactional
    void persistRecipe() {
        UserAccountEntity user = new UserAccountEntity();
        user.username = "testuser";
        user.passwordHash = "hash";
        user.emailAddress = "test@example.com";
        user.persist();

        RecipeEntity recipe = new RecipeEntity();
        recipe.title = "Pasta";
        recipe.description = "Delicious pasta";
        recipe.numServings = 4;
        recipe.author = user;
        recipe.persist();

        Assertions.assertThat(recipe.id).isNotNull().isNotNegative();
        RecipeEntity recipeEntity = RecipeEntity.findById(recipe.id);
        Assertions.assertThat("Pasta").isEqualTo(recipeEntity.title);

        // Clean-up
        user.delete();
        recipe.delete();
    }
}
