package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
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
        private PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

        @Override
        public Map<String, String> start() {
            postgres.withCopyFileToContainer(
                    MountableFile.forHostPath("db/000-init.sql"), "/docker-entrypoint-initdb.d/000-init.sql");
            postgres.withCopyFileToContainer(
                    MountableFile.forHostPath("db/schema/account.sql"),
                    "/docker-entrypoint-initdb.d/schema/account.sql");
            postgres.withCopyFileToContainer(
                    MountableFile.forHostPath("db/schema/ingredient.sql"),
                    "/docker-entrypoint-initdb.d/schema/ingredient.sql");
            postgres.withCopyFileToContainer(
                    MountableFile.forHostPath("db/schema/preparation_step.sql"),
                    "/docker-entrypoint-initdb.d/schema/preparation_step.sql");
            postgres.withCopyFileToContainer(
                    MountableFile.forHostPath("db/schema/recipe.sql"), "/docker-entrypoint-initdb.d/schema/recipe.sql");
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
