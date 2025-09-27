package dev.blaauwendraad.recipe_book.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity extends PanacheEntityBase {

    public RefreshTokenEntity() {
        this.valid = true;
        this.issuedAt = Instant.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("NullAway.Init")
    public Long id;

    @Column(name = "user_id", nullable = false, updatable = false, insertable = false)
    @SuppressWarnings("NullAway.Init")
    public Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @SuppressWarnings("NullAway.Init")
    public UserAccountEntity user;

    @Size(max = 255)
    @Column(name = "token", nullable = false)
    @SuppressWarnings("NullAway.Init")
    public String token;

    @ColumnDefault("now()")
    @Column(name = "issued_at", nullable = false)
    @SuppressWarnings("NullAway.Init")
    public Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    @SuppressWarnings("NullAway.Init")
    public Instant expiresAt;

    @ColumnDefault("true")
    @Column(name = "valid", nullable = false)
    @SuppressWarnings("NullAway.Init")
    public Boolean valid;
}
