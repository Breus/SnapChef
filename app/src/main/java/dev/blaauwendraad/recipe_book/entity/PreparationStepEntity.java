package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "step")
public class PreparationStepEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    public RecipeEntity recipe;

    @Column(columnDefinition = "text", nullable = false)
    public String description;

    @Column(nullable = false)
    public Integer position = 0;
}