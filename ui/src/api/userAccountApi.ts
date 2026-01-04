import type UserAccount from "../models/domain/UserAccount.ts";
import { get, HttpError, put } from "./httpClient.ts";

export const getUser = async (userId: number): Promise<UserAccount> => {
    try {
        const user = await get<UserAccount>(`/users/${userId}`, {auth: "accessToken"});
        return user;
    } catch (error) {
        console.error("Error fetching user: ", error);
        throw error;
    }
}

export const updateEmail = async (userId: number, newEmail: string, currentPassword: string): Promise<void> => {
    try {
        return await put(`/users/${userId}/email`, { newEmail, currentPassword }, {auth: "accessToken"});
    } catch (error) {
        if(error instanceof HttpError) {
            throw new Error(error.data.detail);
        }
        console.error("Error updating email: ", error);
        throw error;
    }
}

export const updatePassword = async (userId: number, currentPassword: string, newPassword: string): Promise<void> => {
    try {
        return await put(`/users/${userId}/password`, { currentPassword, newPassword }, {auth: "accessToken"});
    } catch (error) {
        if(error instanceof HttpError) {
            throw new Error(error.data.detail);
        }
        console.error("Error updating password: ", error);
        throw error;
    }
};