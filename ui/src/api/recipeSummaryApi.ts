import type RecipeSummary from "../models/domain/RecipeSummary.ts";
import type RecipeSummariesResponse from "../models/dto/RecipeSummariesResponse.ts";
import { get } from "./httpClient.ts";

export const getAllRecipeSummaries = async (): Promise<RecipeSummary[]> => {
    try {
        const data = await get<RecipeSummariesResponse>("recipes/summaries");
        return data.recipeSummaries;
    } catch (error) {
        console.error("Error fetching recipes:", error);
        throw error;
    }
};
