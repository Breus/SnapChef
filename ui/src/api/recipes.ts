import type {Recipe} from "../models/Recipe.ts";


const API_BASE_URL = "http://localhost:8080";

/**
 * Fetches all recipes from the backend
 * @returns Promise<Recipe[]> A promise that resolves to an array of recipes
 */
export const getAllRecipes = async (): Promise<Recipe[]> => {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes`);

        if (!response.ok) {
            throw new Error(`Failed to fetch recipes: ${response.status} ${response.statusText}`);
        }

        return await response.json();
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
export const getRecipeById = async (id: number): Promise<Recipe> => {
    try {
        // Since the Quarkus endpoint doesn't have a specific endpoint for getting a recipe by ID,
        // we'll fetch all recipes and find the one with the matching ID
        const recipes = await getAllRecipes();
        const recipe = recipes.find(recipe => recipe.id === id);

        if (!recipe) {
            throw new Error(`Recipe with ID ${id} not found`);
        }

        return recipe;
    } catch (error) {
        console.error(`Error fetching recipe with ID ${id}:`, error);
        throw error;
    }
};
