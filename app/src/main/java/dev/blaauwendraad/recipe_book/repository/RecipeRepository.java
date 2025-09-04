package dev.blaauwendraad.recipe_book.repository;

import dev.blaauwendraad.recipe_book.data.model.RecipeEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecipeRepository implements PanacheRepository<RecipeEntity> {}
