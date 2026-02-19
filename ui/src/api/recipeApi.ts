import type { PreparationTime } from "../models/domain/PreparationTime.ts";
import type Recipe from "../models/domain/Recipe.ts";
import type CreateRecipeResponse from "../models/dto/CreateRecipeResponse.ts";
import type RecipeDto from "../models/dto/RecipeDto.ts";
import { get, request, del } from "./httpClient.ts";

export const getRecipeById = async (id: number): Promise<Recipe> => {
    try {
        const data = await get<RecipeDto>(`recipes/${id}`);
        const recipe: Recipe = {
            title: data.title.trim(),
            description: data.description.trim(),
            imageName: data.imageName,
            numServings: data.numServings,
            preparationTime: data.preparationTime as PreparationTime,
            author: {
                userId: data.author.userId,
                username: data.author.username,
            },
            id: data.id,
            ingredients: data.ingredients,
            preparationSteps: data.preparationSteps,
        };
        return recipe;
    } catch (error) {
        console.error("Error fetching recipe:", error);
        throw error;
    }
};

export const createRecipe = async (recipe: FormData): Promise<number> => {
    try {
        const data = await request<CreateRecipeResponse>("POST", "recipes", recipe, { auth: "accessToken" }, true);
        return data.recipeId;
    } catch (error) {
        console.error("Error creating recipe:", error);
        throw error;
    }
};

export const updateRecipe = async (id: number, recipe: FormData): Promise<void> => {
    try {
        await request<void>("PUT", `recipes/${id}`, recipe, { auth: "accessToken" }, true);
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
