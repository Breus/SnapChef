import type Ingredient from "../domain/Ingredient.ts";
import type { PreparationTime } from "../domain/PreparationTime.ts";
import type PreparationStep from "../domain/PreparationStep.ts";

export default interface RecipeCreateDto {
    title: string;
    description: string;
    numServings: number;
    preparationTime: PreparationTime;
    ingredients: Ingredient[];
    preparationSteps: PreparationStep[];
}
