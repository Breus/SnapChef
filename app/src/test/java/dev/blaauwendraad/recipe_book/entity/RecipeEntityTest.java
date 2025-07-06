package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class RecipeEntityTest {

    @Test
    @Transactional
    void persistRecipe() {
        AccountEntity user = new AccountEntity();
        user.username = "testuser";
        user.passwordHash = "hash";
        user.email = "test@example.com";
        user.persist();

        RecipeEntity recipe = new RecipeEntity();
        recipe.title = "Pasta";
        recipe.description = "Delicious pasta";
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