import { get, HttpError, put } from "./httpClient.ts";

export const getEmail = async (userId: number): Promise<string> => {
    try {
        return await get(`/users/${userId}/email`, {auth: "accessToken"});
    } catch (error) {
        console.error("Error fetching email: ", error);
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