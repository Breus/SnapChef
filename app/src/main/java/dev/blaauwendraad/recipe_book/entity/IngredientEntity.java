package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingredient")
public class IngredientEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("NullAway.Init")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
    @SuppressWarnings("NullAway.Init")
    public RecipeEntity recipe;

    @Column(length = 255, nullable = false)
    @SuppressWarnings("NullAway.Init")
    public String name;

    @Column(length = 100, nullable = false)
    @SuppressWarnings("NullAway.Init")
    public String quantity;

    @Column(nullable = false)
    @SuppressWarnings("NullAway.Init")
    public Integer position = 0;
}
