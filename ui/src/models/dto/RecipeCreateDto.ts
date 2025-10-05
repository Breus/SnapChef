import type Ingredient from "../domain/Ingredient.ts";
import type { PreparationTime } from "../domain/PreparationTime.ts";
import type PreparationStep from "../domain/PreparationStep.ts";

export default interface RecipeCreateDto {
    title: string;
    description: string;
    numServings: number;
<<<<<<< HEAD
    preparationTime: PreparationTime;
=======
    preparationTime: number | null;
>>>>>>> 8fafeab (Added preparation time and number of serving attributes for recipes)
    ingredients: Ingredient[];
    preparationSteps: PreparationStep[];
}
