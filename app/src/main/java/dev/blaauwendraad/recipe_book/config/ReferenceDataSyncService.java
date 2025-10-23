package dev.blaauwendraad.recipe_book.config;

import dev.blaauwendraad.recipe_book.data.model.RoleEntity;
import dev.blaauwendraad.recipe_book.data.model.RoleName;
import dev.blaauwendraad.recipe_book.repository.RoleRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class ReferenceDataSyncService {
    private static final Logger log = Logger.getLogger(ReferenceDataSyncService.class);
    private final RoleRepository roleRepository;
    private final DemoDataSyncService demoDataSyncService;

    @ConfigProperty(name = "quarkus.profile")
    @SuppressWarnings({"NullAway.Init", "UnusedVariable"})
    Optional<String> profile;

    public ReferenceDataSyncService(DemoDataSyncService demoDataSyncService, RoleRepository roleRepository) {
        this.demoDataSyncService = demoDataSyncService;
        this.roleRepository = roleRepository;
    }

    public void syncReferenceData(@Observes StartupEvent startupEvent) {
        syncRoles();
        validateRoles();
        if (profile.isPresent() && "dev".equals(profile.get())) {
            demoDataSyncService.insertDemoData();
        }
    }

    private void validateRoles() {
        List<RoleEntity> roleEntities = roleRepository.listAll();
        if (roleEntities.size() != RoleName.values().length) {
            throw new IllegalStateException("Role table does not contain the expected number of roles.");
        }
    }

    @Transactional
    void syncRoles() {
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName) == null) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.roleName = roleName;
                roleRepository.persistAndFlush(roleEntity);
                log.infof("Inserted role with name %s", roleName);
            }
        }
    }
}
