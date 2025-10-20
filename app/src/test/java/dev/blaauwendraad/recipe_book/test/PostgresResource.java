package dev.blaauwendraad.recipe_book.test;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * A shared test resource that provides a PostgreSQL database with Flyway migrations.
 * This class can be reused across multiple test classes.
 */
public class PostgresResource implements QuarkusTestResourceLifecycleManager {

    private final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest").withStartupTimeout(Duration.ofSeconds(60));

    @Override
    public Map<String, String> start() {
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
