import type LoginCredentials from "../auth/LoginCredentials.ts";
import type AuthenticationDetails from "../auth/AuthenticationDetails.ts";
import type RefreshTokenResponse from "../auth/RefreshTokenResponse.ts";
import { post } from "./httpClient.ts";

export const submitLogin = async (loginCredentials: LoginCredentials): Promise<AuthenticationDetails> => {
    try {
        return await post("/users/authn/login", {loginCredentials});
    } catch (error) {
        console.error("Error logging in user:", error);
        throw error;
    }
};

/**
 * Refresh the access token using a refresh token
 * @param refreshToken The refresh token to use
 * @returns RefreshTokenResponse with the new access token and expiry time
 */
export const refreshAccessToken = async (refreshToken: string): Promise<RefreshTokenResponse> => {
    try {
        return await post("/users/authn/refresh", {refreshToken});
    } catch (error) {
        console.error(error)
        throw error;
    }
};
