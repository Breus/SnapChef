package dev.blaauwendraad.recipe_book.resource.model;

import java.util.List;

public record RecipeSummariesResponse(List<RecipeSummaryDto> recipesSummaries) {
}
