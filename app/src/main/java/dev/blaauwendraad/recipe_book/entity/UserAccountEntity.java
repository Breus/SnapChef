package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "user_account")
public class UserAccountEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 50, unique = true, nullable = false)
    public String username;

    @Column(name = "password_hash", columnDefinition = "text", nullable = false)
    public String passwordHash;

    @Column(length = 100, unique = true)
    public String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_account_role",
            joinColumns = @JoinColumn(name = "user_account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<RoleEntity> roles;
}
