package dev.blaauwendraad.recipe_book.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("NullAway.Init")
    public Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    @SuppressWarnings("NullAway.Init")
    public RoleName roleName;
}
