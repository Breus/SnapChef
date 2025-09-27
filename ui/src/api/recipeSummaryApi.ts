import type RecipeSummary from "../models/domain/RecipeSummary.ts";
import type RecipeSummariesResponse from "../models/dto/RecipeSummariesResponse.ts";
import { get } from "./httpClient.ts";

export const getAllRecipeSummaries = async (recipeSummariesFilter: string,
                                            accessToken: string | null
    ): Promise<RecipeSummary[]> => {
        try {
            const headers: Record<string, string> = {};
            if (recipeSummariesFilter != "ALL" && !accessToken) {
                throw new Error("Authentication token is required for this filter");
            }
            if (accessToken) {
                headers.Authorization = `Bearer ${accessToken}`;
            }
            const data = await get<RecipeSummariesResponse>(`recipes/summaries/${recipeSummariesFilter}`, {headers});
            return data.recipeSummaries;
        } catch (error) {
            console.error("Error fetching recipes:", error);
            throw error;
        }
    }
;
