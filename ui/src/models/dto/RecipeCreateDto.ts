import type Ingredient from "../domain/Ingredient.ts";
import type PreparationStep from "../domain/PreparationStep.ts";

export default interface RecipeCreateDto {
    title: string;
    description: string;
    author: string;
    ingredients: Ingredient[];
    preparationSteps: PreparationStep[];
}
