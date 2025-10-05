import type { PreparationTime } from "./PreparationTime";

export default interface RecipeSummary {
    id: number;
    title: string;
    description: string;
    numServings: number;
<<<<<<< HEAD
    preparationTime: PreparationTime;
=======
    preparationTime: number | null;
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
    author: {
        userId: number;
        username: string;
    };
}
