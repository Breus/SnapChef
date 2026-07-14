import type { PreparationTime } from "./PreparationTime";

export default interface RecipeSummary {
    id: number;
    title: string;
    description: string;
    imageName: string | null;
    numServings: number;
    preparationTime: PreparationTime;
    author: {
        userId: number;
        username: string;
    };
}
