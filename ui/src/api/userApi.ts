import type AuthenticationDetails from "../auth/AuthenticationDetails";
import type LoginCredentials from "../auth/LoginCredentials";
import type UserFavoritesResponse from "../models/dto/UserFavoritesResponse.ts";
import { get, post } from "./httpClient.ts";

export const submitLogin = async (loginCredentials: LoginCredentials): Promise<AuthenticationDetails> => {
    try {
        return await post("/users/login", { loginCredentials });
    } catch (error) {
        console.error("Error logging in user:", error);
        throw error;
    }
};

export const getUserFavoriteRecipesIds = async (userId: number, authToken: string): Promise<number[]> => {
    try {
        const headers = {
            Authorization: `Bearer ${authToken}`,
        };
        const data = await get<UserFavoritesResponse>(`users/${userId.toString()}/recipes/favorites`, { headers });
        return data.favoriteRecipesIds;
    } catch (error) {
        console.error("Error retrieving user favorite recipes ids");
        throw error;
    }
};
