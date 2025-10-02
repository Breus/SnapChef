import type RecipeSummary from "../models/domain/RecipeSummary.ts";
import type RecipeSummariesResponse from "../models/dto/RecipeSummariesResponse.ts";
import { get } from "./httpClient.ts";
import { useAuth } from "../auth/useAuth";

export const getAllRecipeSummaries = async (recipeSummariesFilter: string): Promise<RecipeSummary[]> => {
    try {
        // If the filter is not ALL (so MY or FAVORITES), require authentication
        if (recipeSummariesFilter !== "ALL") {
            const {accessToken} = useAuth();
            if (!accessToken.value) {
                throw new Error("Authentication token is required for this filter");
            }
            const data = await get<RecipeSummariesResponse>(`recipes/summaries/${recipeSummariesFilter}`, {auth: "accessToken"});
            return data.recipeSummaries;
        } else {
            // No auth required for ALL recipes
            const data = await get<RecipeSummariesResponse>(`recipes/summaries/${recipeSummariesFilter}`);
            return data.recipeSummaries;
        }
    } catch (error) {
        console.error("Error fetching recipes:", error);
        throw error;
    }
};
