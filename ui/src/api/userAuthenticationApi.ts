import type AuthenticationDetails from "../auth/AuthenticationDetails.ts";
import type RefreshTokenResponse from "../auth/RefreshTokenResponse.ts";
import { post } from "./httpClient.ts";
import {useAuth} from "../auth/useAuth.ts";
import type RegistrationRequest from "../auth/RegistrationRequest.ts";


export const submitRegistration = async (registrationRequest: RegistrationRequest): Promise<void> => {
    try {
        return await post("/users/authn/register", registrationRequest);
    } catch (error) {
        console.error("Error registering user:", error);
        throw error;
    }
}

export const submitLogin = async (emailAddress: string, password: string): Promise<AuthenticationDetails> => {
    try {
        return await post("/users/authn/login", {emailAddress, password});
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
        useAuth().logout();
        console.error(error);
        throw error;
    }
};
