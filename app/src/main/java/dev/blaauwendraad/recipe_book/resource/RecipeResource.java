package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.dto.RecipeDTO;
import dev.blaauwendraad.recipe_book.resource.model.RecipeAuthorDto;
import dev.blaauwendraad.recipe_book.resource.model.RecipeSummariesResponse;
import dev.blaauwendraad.recipe_book.resource.model.RecipeSummaryDto;
import dev.blaauwendraad.recipe_book.service.RecipeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/recipes")
@ApplicationScoped
public class RecipeResource {
    private final List<RecipeDTO> recipes = RecipeMockData.getMockRecipes();
    private final RecipeService recipeService;

    @Inject
    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GET
    @Path("/summaries")
    @Produces(MediaType.APPLICATION_JSON)
    public RecipeSummariesResponse listRecipe() {
        return new RecipeSummariesResponse(recipeService.getAllRecipeSummaries().stream()
                .map(recipeSummary -> new RecipeSummaryDto(
                        recipeSummary.id(),
                        recipeSummary.title(),
                        recipeSummary.description(),
                        new RecipeAuthorDto(
                                recipeSummary.author().id(),
                                recipeSummary.author().username())))
                .toList());
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
