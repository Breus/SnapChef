package dev.blaauwendraad.recipebook.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "step")
public class PreparationStep extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    public Recipe recipe;

    @Column(columnDefinition = "text", nullable = false)
    public String description;

    @Column(nullable = false)
    public Integer position = 0;
}