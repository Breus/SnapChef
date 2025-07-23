import type RecipeSummary from "../domain/RecipeSummary.ts";

export default interface RecipeSummariesResponse {
    recipeSummaries: RecipeSummary[];
}
