import type { RecipeSummariesResponse } from "../models/RecipeSummariesResponse.ts";
import type { RecipeSummary } from "../models/RecipeSummary.ts";

const API_BASE_URL = import.meta.env.VITE_API_URL;

export const getAllRecipeSummaries = async (): Promise<RecipeSummary[]> => {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/summaries`);

        if (!response.ok) {
            throw new Error(`Failed to fetch recipes: ${response.status} ${response.statusText}`);
        }

        const data: RecipeSummariesResponse = await response.json();
        return data.recipeSummaries;
    } catch (error) {
        console.error("Error fetching recipes:", error);
        throw error;
    }
};

/**
 * Fetches a specific recipe by ID from the Quarkus backend
 * @param id The ID of the recipe to fetch
 * @returns Promise<Recipe> A promise that resolves to a recipe
 */
export const getRecipeById = async (id: number): Promise<RecipeSummary> => {
    try {
        // Since the Quarkus endpoint doesn't have a specific endpoint for getting a recipe by ID,
        // we'll fetch all recipes and find the one with the matching ID
        const recipes = await getAllRecipeSummaries();
        const recipe = recipes.find((recipe) => recipe.id === id);

        if (!recipe) {
            throw new Error(`Recipe with ID ${id} not found`);
        }

        return recipe;
    } catch (error) {
        console.error(`Error fetching recipe with ID ${id}:`, error);
        throw error;
    }
};
