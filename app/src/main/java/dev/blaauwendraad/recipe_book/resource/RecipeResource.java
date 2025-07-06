package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.dto.RecipeDTO;
import jakarta.ws.rs.*;

import java.util.List;

@Path("/recipes")
public class RecipeResource {
	private final List<RecipeDTO> recipes = RecipeMockData.getMockRecipes();

	@GET
	public List<RecipeDTO> list() {
		return recipes;
	}

	@POST
	public List<RecipeDTO> add(RecipeDTO recipe) {
		recipes.add(recipe);
		return recipes;
	}

	@DELETE
	public List<RecipeDTO> remove(RecipeDTO recipe) {
		recipes.removeIf(existingRecipe -> existingRecipe.id().equals(recipe.id()));
		return recipes;
	}
}
