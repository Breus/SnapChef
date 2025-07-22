import type { Recipe } from "../models/Recipe.ts";
import type { RecipeResponse } from "../models/RecipeResponse.ts";

export const API_BASE_URL = import.meta.env.VITE_API_URL;

export const getRecipeById = async (id: number): Promise<Recipe> => {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/${id}`);

        if (!response.ok) {
            throw new Error(`Recipe with ID ${id} not found`);
        }

        const data: RecipeResponse = await response.json();
        return data.recipe;
    } catch (error) {
        console.error(`Error fetching recipe with ID ${id}:`, error);
        throw error;
    }
};

export const createRecipe = async (recipe: Recipe): Promise<Recipe> => {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ recipe }),
        });

        if (!response.ok) {
            throw new Error(`Failed to create recipe: ${response.status} ${response.statusText}`);
        }

        const data: RecipeResponse = await response.json();
        return data.recipe;
    } catch (error) {
        console.error("Error creating recipe:", error);
        throw error;
    }
};
