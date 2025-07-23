import type Recipe from "../models/domain/Recipe.ts";
import type CreateRecipeResponse from "../models/dto/CreateRecipeResponse.ts";
import type RecipeCreateDto from "../models/dto/RecipeCreateDto.ts";
import type RecipeResponse from "../models/dto/RecipeResponse.ts";
import { get, post } from "./httpClient.ts";

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
        const data = await post<CreateRecipeResponse>("recipes", { recipe });
        return data.recipeId;
    } catch (error) {
        console.error("Error creating recipe:", error);
        throw error;
    }
};
