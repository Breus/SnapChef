package dev.blaauwendraad.recipe_book.data;

import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@QuarkusTest
@QuarkusTestResource(RecipeEntityTest.PostgresTestResource.class)
class RecipeEntityTest {

    public static class PostgresTestResource implements QuarkusTestResourceLifecycleManager {
        private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
                .withStartupTimeout(Duration.ofSeconds(60)); // Add explicit timeout

        @Override
        public Map<String, String> start() {
            postgres.withCopyFileToContainer(MountableFile.forHostPath("db"), "/docker-entrypoint-initdb.d/");
            postgres.start();
            Map<String, String> config = new HashMap<>();
            config.put("quarkus.datasource.jdbc.url", postgres.getJdbcUrl());
            config.put("quarkus.datasource.username", postgres.getUsername());
            config.put("quarkus.datasource.password", postgres.getPassword());
            return config;
        }

        @Override
        public void stop() {
            postgres.stop();
        }
    }

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
