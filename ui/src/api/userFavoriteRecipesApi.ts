import type UserFavoritesResponse from "../models/dto/UserFavoritesResponse.ts";
import { del, get, post } from "./httpClient.ts";

export const getUserFavoriteRecipesIds = async (userId: number): Promise<number[]> => {
    try {
        const data = await get<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites`, {auth: "accessToken"});
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error retrieving user favorite recipes ids", error);
        throw error;
    }
};

export const addRecipeToUserFavorites = async (userId: number, recipeId: number): Promise<number[]> => {
    try {
        const data = await post<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites`, {recipeId: recipeId}, {auth: "accessToken"});
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error adding recipe to user favorites", error);
        throw error;
    }
}

export const removeRecipeFromUserFavorites = async (userId: number, recipeId: number): Promise<number[]> => {
    try {
        const data = await del<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites/${recipeId}`, {auth: "accessToken"});
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error removing recipe from user favorites", error);
        throw error;
    }
};
