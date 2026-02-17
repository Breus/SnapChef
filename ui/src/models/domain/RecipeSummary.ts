import type { PreparationTime } from "./PreparationTime";

export default interface RecipeSummary {
    id: number;
    title: string;
    description: string;
    hasImage: boolean;
    numServings: number;
    preparationTime: PreparationTime;
    author: {
        userId: number;
        username: string;
    };
}
