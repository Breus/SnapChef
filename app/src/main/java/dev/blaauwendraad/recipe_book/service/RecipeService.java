package dev.blaauwendraad.recipe_book.service;

import dev.blaauwendraad.recipe_book.entity.RecipeEntity;
import dev.blaauwendraad.recipe_book.service.model.Author;
import dev.blaauwendraad.recipe_book.service.model.RecipeSummary;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeService {

    public List<RecipeSummary> getAllRecipeSummaries() {
        return RecipeEntity.<RecipeEntity>listAll().stream()
                .map(recipeEntity -> new RecipeSummary(
                        recipeEntity.id,
                        recipeEntity.title,
                        recipeEntity.description,
                        new Author(recipeEntity.author.id, recipeEntity.author.username)
                ))
                .collect(Collectors.toList());
    }
}
