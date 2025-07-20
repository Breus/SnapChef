package dev.blaauwendraad.recipe_book.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "recipe")
public class RecipeEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("NullAway.Init")
    public Long id;

    @Column(nullable = false)
    @SuppressWarnings("NullAway.Init")
    public String title;

    @Column(columnDefinition = "text")
    @Nullable
    public String description;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @Nullable
    public UserAccountEntity author;

    @OneToMany(mappedBy = "recipe")
    @OrderBy("position ASC")
    public List<IngredientEntity> ingredients;

    @OneToMany(mappedBy = "recipe")
    @OrderBy("position ASC")
    public List<PreparationStepEntity> preparationSteps;
}
