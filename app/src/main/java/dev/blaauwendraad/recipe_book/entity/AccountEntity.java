package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class AccountEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 50, unique = true, nullable = false)
    public String username;

    @Column(name = "password_hash", columnDefinition = "text", nullable = false)
    public String passwordHash;

    @Column(length = 100, unique = true)
    public String email;
}