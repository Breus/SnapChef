package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.dto.RecipeDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/recipes")
public class RecipeResource {
	private final List<RecipeDTO> recipes = RecipeMockData.getMockRecipes();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<RecipeDTO> list() {
		return recipes;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<RecipeDTO> add(RecipeDTO recipe) {
		recipes.add(recipe);
		return recipes;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<RecipeDTO> remove(RecipeDTO recipe) {
		recipes.removeIf(existingRecipe -> existingRecipe.id().equals(recipe.id()));
		return recipes;
	}
}
