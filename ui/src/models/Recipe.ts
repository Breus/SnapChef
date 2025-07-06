import type {Ingredient} from "./Ingredient.ts";
import type {Step} from "./Step.ts";

export interface Recipe {
    id: number;
    title: string;
    description: string;
    author: string;
    ingredients: Ingredient[];
    steps: Step[];
}
