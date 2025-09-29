package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.RefreshTokenEntity;
import dev.blaauwendraad.recipe_book.data.model.UserAccountEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshTokenEntity> {

    public RefreshTokenEntity findByToken(String token) {
        return find("token", token).firstResult();
    }

    @Transactional
    public RefreshTokenEntity addRefreshToken(UserAccountEntity userAccountEntity, String token, Instant expiresAt) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.userId = userAccountEntity.id;
        refreshTokenEntity.token = token;
        refreshTokenEntity.expiresAt = expiresAt;
        refreshTokenEntity.persist();
        return refreshTokenEntity;
    }
}
