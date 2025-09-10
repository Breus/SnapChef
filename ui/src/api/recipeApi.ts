import type Recipe from "../models/domain/Recipe.ts";
import type CreateRecipeResponse from "../models/dto/CreateRecipeResponse.ts";
import type RecipeCreateDto from "../models/dto/RecipeCreateDto.ts";
import type RecipeResponse from "../models/dto/RecipeResponse.ts";
import { del, get, post, put } from "./httpClient.ts";

export const getRecipeById = async (id: number): Promise<Recipe> => {
    try {
        const data = await get<RecipeResponse>(`recipes/${id}`);
        return data.recipe;
    } catch (error) {
        console.error("Error fetching recipe:", error);
        throw error;
    }
};

export const createRecipe = async (recipe: RecipeCreateDto, authToken: string): Promise<number> => {
    try {
        const headers = {
            Authorization: `Bearer ${authToken}`,
        };
        const data = await post<CreateRecipeResponse>("recipes", recipe, { headers });
        return data.recipeId;
    } catch (error) {
        console.error("Error creating recipe:", error);
        throw error;
    }
};

export const updateRecipe = async (id: number, recipe: RecipeCreateDto, authToken: string): Promise<void> => {
    try {
        const headers = {
            Authorization: `Bearer ${authToken}`,
        };
        await put<void>(`recipes/${id}`, recipe, { headers });
    } catch (error) {
        console.error("Error updating recipe:", error);
        throw error;
    }
};

export const deleteRecipeById = async (id: number, authToken: string): Promise<void> => {
    try {
        const headers = {
            Authorization: `Bearer ${authToken}`,
        };
        await del<void>(`recipes/${id}`, { headers });
    } catch (error) {
        console.error("Error deleting recipe:", error);
        throw error;
    }
};
