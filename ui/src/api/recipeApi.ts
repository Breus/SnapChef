import type Recipe from "../models/domain/Recipe.ts";
import type CreateRecipeResponse from "../models/dto/CreateRecipeResponse.ts";
import type RecipeCreateDto from "../models/dto/RecipeCreateDto.ts";
import type RecipeResponse from "../models/dto/RecipeResponse.ts";
import { get, post, put, del } from "./httpClient.ts";

export const getRecipeById = async (id: number): Promise<Recipe> => {
    try {
        const data = await get<RecipeResponse>(`recipes/${id}`);
        return data.recipe;
    } catch (error) {
        console.error("Error fetching recipe:", error);
        throw error;
    }
};

export const createRecipe = async (recipe: RecipeCreateDto): Promise<number> => {
    try {
        const data = await post<CreateRecipeResponse>("recipes", recipe, { auth: "accessToken" });
        return data.recipeId;
    } catch (error) {
        console.error("Error creating recipe:", error);
        throw error;
    }
};

export const updateRecipe = async (id: number, recipe: RecipeCreateDto): Promise<void> => {
    try {
        await put<void>(`recipes/${id}`, recipe, { auth: "accessToken" });
    } catch (error) {
        console.error("Error updating recipe:", error);
        throw error;
    }
};

export const deleteRecipeById = async (id: number): Promise<void> => {
    try {
        await del<void>(`recipes/${id}`, { auth: "accessToken" });
    } catch (error) {
        console.error("Error deleting recipe:", error);
        throw error;
    }
};
