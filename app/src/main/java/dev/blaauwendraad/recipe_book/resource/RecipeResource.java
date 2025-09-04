package dev.blaauwendraad.recipe_book.resource;

import dev.blaauwendraad.recipe_book.resource.model.CreateRecipeRequestDto;
import dev.blaauwendraad.recipe_book.resource.model.CreateRecipeResponseDto;
import dev.blaauwendraad.recipe_book.resource.model.IngredientDto;
import dev.blaauwendraad.recipe_book.resource.model.PreparationStepDto;
import dev.blaauwendraad.recipe_book.resource.model.RecipeAuthorDto;
import dev.blaauwendraad.recipe_book.resource.model.RecipeDto;
import dev.blaauwendraad.recipe_book.resource.model.RecipeResponse;
import dev.blaauwendraad.recipe_book.resource.model.RecipeSummariesResponse;
import dev.blaauwendraad.recipe_book.resource.model.RecipeSummaryDto;
import dev.blaauwendraad.recipe_book.service.RecipeService;
import dev.blaauwendraad.recipe_book.service.model.Ingredient;
import dev.blaauwendraad.recipe_book.service.model.PreparationStep;
import dev.blaauwendraad.recipe_book.service.model.Recipe;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/recipes")
@ApplicationScoped
public class RecipeResource {
    private final RecipeService recipeService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GET
    @Path("/summaries")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public RecipeSummariesResponse listRecipe() {
        return new RecipeSummariesResponse(recipeService.getAllRecipeSummaries().stream()
                .map(recipeSummary -> new RecipeSummaryDto(
                        recipeSummary.id(),
                        recipeSummary.title(),
                        recipeSummary.description(),
                        recipeSummary.author() == null
                                ? null
                                : new RecipeAuthorDto(
                                        recipeSummary.author().id(),
                                        recipeSummary.author().authorName())))
                .toList());
    }

    @GET
    @Path("/{id}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public RecipeResponse getRecipe(@PathParam("id") Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe not found with id: " + id);
        }

        return new RecipeResponse(new RecipeDto(
                recipe.id(),
                recipe.title(),
                recipe.description(),
                recipe.author() == null ? null : recipe.author().authorName(),
                recipe.ingredients().stream()
                        .map(ingredient -> new IngredientDto(ingredient.name(), ingredient.quantity()))
                        .toList(),
                recipe.preparationSteps().stream()
                        .map(step -> new PreparationStepDto(step.description()))
                        .toList()));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public CreateRecipeResponseDto add(@NotNull @Valid CreateRecipeRequestDto newRecipe) {
        Long recipeId = recipeService.createRecipe(
                newRecipe.title(),
                newRecipe.description(),
                Long.valueOf(jwt.getClaim("upn")),
                newRecipe.ingredients().stream()
                        .map(ingredientDto -> new Ingredient(ingredientDto.name(), ingredientDto.quantity()))
                        .toList(),
                newRecipe.preparationSteps().stream()
                        .map(stepDto -> new PreparationStep(stepDto.description()))
                        .toList());
        return new CreateRecipeResponseDto(recipeId);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public List<RecipeDto> remove(RecipeDto recipe) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
