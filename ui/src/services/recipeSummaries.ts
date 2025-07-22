import type { RecipeSummariesResponse } from "../models/RecipeSummariesResponse.ts";
import type { RecipeSummary } from "../models/RecipeSummary.ts";

const API_BASE_URL = import.meta.env.VITE_API_URL;

export const getAllRecipeSummaries = async (): Promise<RecipeSummary[]> => {
    try {
        const response = await fetch(`${API_BASE_URL}/recipes/summaries`);

        if (!response.ok) {
            throw new Error(`Failed to fetch recipes: ${response.status} ${response.statusText}`);
        }

        const data: RecipeSummariesResponse = await response.json();
        return data.recipeSummaries;
    } catch (error) {
        console.error("Error fetching recipes:", error);
        throw error;
    }
};
