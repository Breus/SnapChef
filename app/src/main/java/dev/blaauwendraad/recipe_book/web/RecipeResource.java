package dev.blaauwendraad.recipe_book.web;

import dev.blaauwendraad.recipe_book.service.ImageService;
import dev.blaauwendraad.recipe_book.service.RecipeService;
import dev.blaauwendraad.recipe_book.service.model.Ingredient;
import dev.blaauwendraad.recipe_book.service.model.PreparationStep;
import dev.blaauwendraad.recipe_book.service.model.Recipe;
import dev.blaauwendraad.recipe_book.web.model.IngredientDto;
import dev.blaauwendraad.recipe_book.web.model.PreparationStepDto;
import dev.blaauwendraad.recipe_book.web.model.RecipeAuthorDto;
import dev.blaauwendraad.recipe_book.web.model.RecipeDto;
import dev.blaauwendraad.recipe_book.web.model.RecipeResponse;
import dev.blaauwendraad.recipe_book.web.model.RecipeSummariesFilter;
import dev.blaauwendraad.recipe_book.web.model.RecipeSummariesResponse;
import dev.blaauwendraad.recipe_book.web.model.RecipeSummaryDto;
import dev.blaauwendraad.recipe_book.web.model.SaveRecipeRequestDto;
import dev.blaauwendraad.recipe_book.web.model.SaveRecipeResponseDto;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
@Path("/api/recipes")
public class RecipeResource {
    private final RecipeService recipeService;
    private final ImageService imageService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public RecipeResource(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GET
    @Path("/summaries/{filter}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public RecipeSummariesResponse getRecipeSummaries(@PathParam("filter") RecipeSummariesFilter filter) {
        String upn = jwt.getName();
        Long userId = null;
        if (upn == null) {
            if (filter == RecipeSummariesFilter.MY) {
                throw new ForbiddenException("Cannot access my recipes without user authentication");
            }
            if (filter == RecipeSummariesFilter.FAVORITES) {
                throw new ForbiddenException("Cannot access favorite recipes without user authentication");
            }
        } else {
            userId = Long.valueOf(upn);
        }
        return new RecipeSummariesResponse(recipeService.getAllRecipeSummaries(filter, userId).stream()
                .map(recipeSummary -> new RecipeSummaryDto(
                        recipeSummary.id(),
                        recipeSummary.title(),
                        recipeSummary.description(),
                        recipeSummary.imageName(),
                        recipeSummary.numServings(),
                        recipeSummary.preparationTime(),
                        recipeSummary.author() == null
                                ? null
                                : new RecipeAuthorDto(
                                        recipeSummary.author().id().toString(),
                                        recipeSummary.author().authorName())))
                .toList());
    }

    @GET
    @Path("/{recipeId}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public RecipeResponse getRecipe(@PathParam("recipeId") Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe not found with recipeId: " + id);
        }

        return new RecipeResponse(new RecipeDto(
                recipe.id(),
                recipe.title(),
                recipe.description(),
                recipe.imageName(),
                recipe.numServings(),
                recipe.preparationTime(),
                recipe.author() == null
                        ? null
                        : new RecipeAuthorDto(
                                recipe.author().id().toString(), recipe.author().authorName()),
                recipe.ingredients().stream()
                        .map(ingredient -> new IngredientDto(ingredient.description()))
                        .toList(),
                recipe.preparationSteps().stream()
                        .map(step -> new PreparationStepDto(step.description()))
                        .toList()));
    }

    @GET
    @Path("/{recipeId}/image")
    @PermitAll
    @Produces("image/jpeg")
    public Response getRecipeImage(@PathParam("recipeId") Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            throw new NotFoundException("Recipe not found with recipeId: " + id);
        }
        if (recipe.imageName() == null) {
            throw new NotFoundException("Recipe with recipeId: " + id + " has no image");
        }
        byte[] imageFile = imageService.getObject(recipe.imageName());
        if (imageFile == null) {
            throw new NotFoundException("Image not found for recipeId: " + id);
        }
        return Response.ok(imageFile).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public SaveRecipeResponseDto createRecipe(@NotNull @Valid SaveRecipeRequestDto newRecipe) {
        Long recipeId = recipeService.createRecipe(
                newRecipe.title(),
                newRecipe.description(),
                newRecipe.imageName(),
                newRecipe.numServings(),
                newRecipe.preparationTime(),
                Long.valueOf(jwt.getName()),
                newRecipe.ingredients().stream()
                        .map(ingredientDto -> new Ingredient(ingredientDto.description()))
                        .toList(),
                newRecipe.preparationSteps().stream()
                        .map(stepDto -> new PreparationStep(stepDto.description()))
                        .toList());
        return new SaveRecipeResponseDto(recipeId);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{recipeId}")
    @RolesAllowed({"admin", "user"})
    public SaveRecipeResponseDto updateRecipe(
            @PathParam("recipeId") Long recipeId, @NotNull @Valid SaveRecipeRequestDto updatedRecipe) {
        recipeService.updateRecipe(
                recipeId,
                updatedRecipe.title(),
                updatedRecipe.description(),
                updatedRecipe.imageName(),
                updatedRecipe.numServings(),
                updatedRecipe.preparationTime(),
                Long.valueOf(jwt.getName()),
                updatedRecipe.ingredients().stream()
                        .map(ingredientDto -> new Ingredient(ingredientDto.description()))
                        .toList(),
                updatedRecipe.preparationSteps().stream()
                        .map(stepDto -> new PreparationStep(stepDto.description()))
                        .toList());
        return new SaveRecipeResponseDto(recipeId);
    }

    @DELETE
    @Path("/{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public Response deleteRecipe(@PathParam("recipeId") Long recipeId) {
        recipeService.deleteRecipe(recipeId, Long.valueOf(jwt.getName()));
        return Response.noContent().build();
    }
}
