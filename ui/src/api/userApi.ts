import type AuthenticationDetails from "../auth/AuthenticationDetails";
import type LoginCredentials from "../auth/LoginCredentials";
import type UserFavoritesResponse from "../models/dto/UserFavoritesResponse.ts";
import { del, get, post } from "./httpClient.ts";

export const submitLogin = async (loginCredentials: LoginCredentials): Promise<AuthenticationDetails> => {
    try {
        return await post("/users/login", {loginCredentials});
    } catch (error) {
        console.error("Error logging in user:", error);
        throw error;
    }
};

export const getUserFavoriteRecipesIds = async (userId: number, accessToken: string): Promise<number[]> => {
    try {
        const headers = {
            Authorization: `Bearer ${accessToken}`,
        };
        const data = await get<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites`, {headers});
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error retrieving user favorite recipes ids", error);
        throw error;
    }
};

export const addRecipeToUserFavorites = async (userId: number, recipeId: number, accessToken: string): Promise<number[]> => {
    try {
        const headers = {
            Authorization: `Bearer ${accessToken}`,
        };
        const data = await post<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites`, {recipeId: recipeId}, {headers});
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error adding recipe to user favorites", error);
        throw error;
    }
}

export const removeRecipeFromUserFavorites = async (userId: number, recipeId: number, accessToken: string): Promise<number[]> => {
    try {
        const headers = {
            Authorization: `Bearer ${accessToken}`,
        };
        const data = await del<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites/${recipeId}`, {headers});
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error removing recipe from user favorites", error);
        throw error;
    }
}

