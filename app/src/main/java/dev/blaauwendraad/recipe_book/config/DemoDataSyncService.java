package dev.blaauwendraad.recipe_book.config;

import dev.blaauwendraad.recipe_book.service.ImageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class DemoDataSyncService {
    private static final Logger log = Logger.getLogger(DemoDataSyncService.class);
    private ImageService imageService;

    private final String jdbcUrl;
    private final String username;
    private final String password;

    @Inject
    public DemoDataSyncService(
            @ConfigProperty(name = "quarkus.datasource.jdbc.url") String jdbcUrl,
            @ConfigProperty(name = "quarkus.datasource.username") String username,
            @ConfigProperty(name = "quarkus.datasource.password") String password,
            ImageService imageService) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.imageService = imageService;
    }

    @Transactional
    void insertDemoData() {
        try {
            log.info("Running demo data insertions...");
            Flyway flyway = Flyway.configure()
                    .dataSource(jdbcUrl, username, password)
                    .locations("classpath:db/dev-data")
                    .table("flyway_schema_history_demo") // Use separate table to avoid conflicts
                    .baselineOnMigrate(true)
                    .baselineVersion("1") // Schema version (1) is the baseline version
                    .load();

            int migrationsApplied = flyway.migrate().migrationsExecuted;
            if (migrationsApplied > 0) {
                log.infof("Applied %d demo data migrations", migrationsApplied);
            } else {
                log.info("No new demo data migrations to apply");
            }
        } catch (Exception e) {
            log.error("Failed to run demo data migrations", e);
            throw new RuntimeException("Demo data migration failed", e);
        }
    }

    void insertDemoImage() {
        File imageFile = new File("data/images/spaghetti.jpg");
        imageService.putObject(imageFile);
        log.info("Inserted demo image into Garage/S3");
    }
}
