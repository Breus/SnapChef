import type Ingredient from "./Ingredient.ts";
import type { PreparationTime } from "./PreparationTime.ts";
import type PreparationStep from "./PreparationStep.ts";

export default interface Recipe {
    id: number;
    title: string;
    description: string;
    numServings: number;
<<<<<<< HEAD
    preparationTime: PreparationTime;
=======
    preparationTime: number;
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
    author: {
        userId: number;
        username: string;
    };
    ingredients: Ingredient[];
    preparationSteps: PreparationStep[];
}
