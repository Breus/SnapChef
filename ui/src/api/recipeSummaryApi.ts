import type RecipeSummary from "../models/domain/RecipeSummary.ts";
import type RecipeSummariesResponse from "../models/dto/RecipeSummariesResponse.ts";
import { get } from "./httpClient.ts";

export const getAllRecipeSummaries = async (recipeSummariesFilter: string,
                                            authToken: string | null
    ): Promise<RecipeSummary[]> => {
        try {
            const headers: Record<string, string> = {};
            if (recipeSummariesFilter != "ALL" && !authToken) {
                throw new Error("Authentication token is required for this filter");
            }
            if (authToken) {
                headers.Authorization = `Bearer ${authToken}`;
            }
            const data = await get<RecipeSummariesResponse>(`recipes/summaries/${recipeSummariesFilter}`, {headers});
            return data.recipeSummaries;
        } catch (error) {
            console.error("Error fetching recipes:", error);
            throw error;
        }
    }
;
