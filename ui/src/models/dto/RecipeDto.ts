import type Ingredient from "../domain/Ingredient.ts";
import type PreparationStep from "../domain/PreparationStep.ts";
import type { PreparationTime } from "../domain/PreparationTime.ts";

export default interface RecipeDto {
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
    ingredients: Ingredient[];
    preparationSteps: PreparationStep[];
}
