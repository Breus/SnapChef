package dev.blaauwendraad.recipe_book.data.model;

import dev.blaauwendraad.recipe_book.service.model.PreparationTime;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
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

    @Column(name = "num_servings")
    @SuppressWarnings("NullAway.Init")
    public Integer numServings;

    @Enumerated(EnumType.STRING)
    @Column(name = "preparation_time")
    @SuppressWarnings("NullAway.Init")
    public PreparationTime preparationTime;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @Nullable
    public UserAccountEntity author;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    public List<IngredientEntity> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    public List<PreparationStepEntity> preparationSteps = new ArrayList<>();
}
