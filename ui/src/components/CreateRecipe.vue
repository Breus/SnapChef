<script setup lang="ts">
import {onMounted, ref, nextTick} from "vue";
import {useRoute, useRouter} from "vue-router";
import {createRecipe, getRecipeById, updateRecipe} from "../api/recipeApi.ts";
import type Ingredient from "../models/domain/Ingredient.ts";
import type PreparationStep from "../models/domain/PreparationStep.ts";
import type RecipeCreateDto from "../models/dto/RecipeCreateDto.ts";
import type Recipe from "../models/domain/Recipe.ts";
import {useAuth} from "../auth/useAuth.ts";
import {PreparationTime} from "../models/domain/PreparationTime.ts";
import { VueDraggableNext } from 'vue-draggable-next';

const router = useRouter();
const route = useRoute();
const isEditMode = !!route.params.id;
const recipeId = route.params.id as string | undefined;
const {isLoggedIn} = useAuth();

// Form data
const title = ref("");
const description = ref("");
const numServings = ref<number>(4);
const preparationTime = ref<PreparationTime>(PreparationTime.MIN_15_30);
const ingredients = ref<Ingredient[]>([{description: ""}]);
const preparationSteps = ref<PreparationStep[]>([{description: ""}]);

// Form state
const isSubmitting = ref(false);
const error = ref<string | null>(null);

// Drag and drop state
const ingredientsReorderMode = ref(false);
const stepsReorderMode = ref(false);
const isMobile = ref(false);

// Check if device is mobile
const checkMobile = () => {
    isMobile.value = window.innerWidth < 768;
};

// Auto-resize function for textareas
const autoResize = (element: HTMLTextAreaElement) => {
    // Temporarily set height to auto to get proper scrollHeight
    element.style.height = 'auto';
    
    // Get the actual scrollHeight which accounts for both content and placeholder
    let newHeight = element.scrollHeight;
    
    // If textarea is empty, ensure it's tall enough for the placeholder
    if (!element.value.trim()) {
        // Temporarily set the placeholder as value to measure its height
        const originalValue = element.value;
        element.value = element.placeholder;
        element.style.height = 'auto';
        const placeholderHeight = element.scrollHeight;
        
        // Restore original value
        element.value = originalValue;
        newHeight = Math.max(placeholderHeight, 44);
    }
    
    element.style.height = newHeight + 'px';
};

// Handle ingredient input changes
const onIngredientInput = (index: number, value: string, event: Event) => {
    if (ingredients.value[index]) {
        ingredients.value[index].description = value;
    }

    // Auto-resize the input field if it's a textarea
    const target = event.target as HTMLTextAreaElement;
    if (target.tagName === 'TEXTAREA') {
        autoResize(target);
    }

    // If typing in the last field and it's not empty, add a new field
    if (index === ingredients.value.length - 1 && value.trim()) {
        ingredients.value.push({description: ""});
        // Auto-resize all textareas after the new field is added to the DOM
        nextTick(() => {
            autoResizeAllTextareas();
        });
    }

    // If field becomes empty and it's not the only one and not the last one, remove it
    if (!value.trim() && ingredients.value.length > 1 && index !== ingredients.value.length - 1) {
        ingredients.value.splice(index, 1);
    }
};

// Remove an ingredient field
const removeIngredient = (index: number) => {
    if (ingredients.value.length > 1) {
        ingredients.value.splice(index, 1);
    }
};

// Handle preparation step input changes
const onStepInput = (index: number, value: string, event: Event) => {
    if (preparationSteps.value[index]) {
        preparationSteps.value[index].description = value;
    }

    // Auto-resize the textarea
    const target = event.target as HTMLTextAreaElement;
    autoResize(target);

    // If typing in the last field and it's not empty, add a new field
    if (index === preparationSteps.value.length - 1 && value.trim()) {
        preparationSteps.value.push({description: ""});
        // Auto-resize all textareas after the new field is added to the DOM
        nextTick(() => {
            autoResizeAllTextareas();
        });
    }

    // If field becomes empty and it's not the only one and not the last one, remove it
    if (!value.trim() && preparationSteps.value.length > 1 && index !== preparationSteps.value.length - 1) {
        preparationSteps.value.splice(index, 1);
    }
};

// Remove a preparation step field
const removeStep = (index: number) => {
    if (preparationSteps.value.length > 1) {
        preparationSteps.value.splice(index, 1);
    }
};

// Handle drag end event for ingredients
const onIngredientDragEnd = () => {
    // The array is automatically updated by VueDraggableNext
    // We just need to trigger a re-render if needed
    nextTick(() => {
        autoResizeAllTextareas();
    });
};

// Handle drag end event for preparation steps
const onStepDragEnd = () => {
    // The array is automatically updated by VueDraggableNext
    // We just need to trigger a re-render if needed
    nextTick(() => {
        autoResizeAllTextareas();
    });
};

// Toggle reorder modes
const toggleIngredientsReorder = () => {
    ingredientsReorderMode.value = !ingredientsReorderMode.value;
    if (ingredientsReorderMode.value) {
        stepsReorderMode.value = false; // Turn off other reorder mode
    }
};

const toggleStepsReorder = () => {
    stepsReorderMode.value = !stepsReorderMode.value;
    if (stepsReorderMode.value) {
        ingredientsReorderMode.value = false; // Turn off other reorder mode
    }
};

// Auto-resize all textareas in the form
const autoResizeAllTextareas = () => {
    nextTick(() => {
        const allTextareas = document.querySelectorAll('.ingredient-textarea, .step-textarea');
        allTextareas.forEach(textarea => {
            autoResize(textarea as HTMLTextAreaElement);
        });
    });
};

onMounted(async () => {
    // Check if mobile on mount
    checkMobile();
    window.addEventListener('resize', checkMobile);

    if (isEditMode && recipeId) {
        try {
            const recipe: Recipe = await getRecipeById(Number(recipeId));
            title.value = recipe.title;
            description.value = recipe.description;
            numServings.value = recipe.numServings;
            preparationTime.value = PreparationTime[recipe.preparationTime as keyof typeof PreparationTime];
            ingredients.value = [...recipe.ingredients, {description: ""}];
            preparationSteps.value = [...recipe.preparationSteps, {description: ""}];

            // Auto-resize all textareas after loading the data
            nextTick(() => {
                autoResizeAllTextareas();
            });
        } catch (err) {
            error.value = "Failed to load recipe for editing.";
        }
    } else {
        // Auto-resize textareas on initial mount for new recipes
        nextTick(() => {
            autoResizeAllTextareas();
        });
    }
});

const submitForm = async () => {
    try {
        isSubmitting.value = true;
        error.value = null;

        if (!title.value.trim()) {
            throw new Error("Title is required");
        }

        // Filter out empty ingredients and steps
        const validIngredients = ingredients.value.filter(ing => ing.description.trim());
        const validSteps = preparationSteps.value.filter(step => step.description.trim());

        if (validIngredients.length === 0) {
            throw new Error("At least one ingredient is required");
        }
        if (validSteps.length === 0) {
            throw new Error("At least one instruction step is required");
        }
        // Map the selected (label) preparationTime back to the enum key
        const preparationTimeKey = (Object.keys(PreparationTime) as Array<keyof typeof PreparationTime>).find(
            (k) => PreparationTime[k] === preparationTime.value
        );
        if (!preparationTimeKey) {
            throw new Error("A preparation time interval must be given");
        }
        if (numServings.value === null || numServings.value === undefined || numServings.value < 1 || numServings.value > 100) {
            throw new Error("Number of servings must be between 1 and 100");
        }

        const newRecipe: RecipeCreateDto = {
            title: title.value.trim(),
            description: description.value.trim(),
            numServings: numServings.value,
            preparationTime: preparationTimeKey as PreparationTime,
            ingredients: validIngredients,
            preparationSteps: validSteps,
        };

        if (!isLoggedIn()) {
            throw new Error("You must be logged in to create a recipe.");
        }
        let goToRecipeId: number;
        if (isEditMode && recipeId) {
            // Update existing recipe
            await updateRecipe(Number(recipeId), newRecipe);
            goToRecipeId = Number(recipeId);
        } else {
            goToRecipeId = await createRecipe(newRecipe);
        }
        router.push(`/recipe/${goToRecipeId}`);
    } catch (err) {
        if (err instanceof Error) {
            error.value = err.message;
        } else {
            error.value = "An unexpected error occurred";
        }
        console.error("Failed to save recipe:", err);
    } finally {
        isSubmitting.value = false;
    }
};

const cancelEdit = () => {
    if (isEditMode && recipeId) {
        router.push(`/recipe/${recipeId}`);
    } else {
        router.push("/");
    }
};
</script>
<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-4xl px-2 sm:px-6 lg:px-8">
            <!-- Back Button -->
            <div class="mb-6">
                <button @click="$router.push('/')"
                        class="inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50">
                    <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                    </svg>
                    Back to Recipes
                </button>
            </div>

            <!-- Form Header -->
            <div class="mb-8 text-center">
                <h1 class="text-3xl font-bold text-gray-900">
                    {{ isEditMode ? 'Edit Recipe' : 'Create New Recipe' }}
                </h1>
                <p class="mt-2 text-gray-600">
                    {{
                        isEditMode ? 'Change the ingredients and steps for your recipe.' : 'Add the ingredients and steps for your recipe.'
                    }}
                </p>
            </div>

            <!-- Error Message -->
            <div v-if="error" class="mb-6 rounded-md bg-red-50 p-4">
                <div class="flex">
                    <div class="flex-shrink-0">
                        <svg class="h-5 w-5 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                        </svg>
                    </div>
                    <div class="ml-3">
                        <h3 class="text-sm font-medium text-red-800">{{ error }}</h3>
                    </div>
                </div>
            </div>

            <!-- Recipe Form -->
            <form @submit.prevent="submitForm" class="overflow-hidden rounded-lg bg-white shadow-lg">
                <div class="p-4 sm:p-6">
                    <!-- Basic Info Section -->
                    <div class="mb-8">
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">Basic Information</h2>

                        <div class="mb-4">
                            <label for="title"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Title</label>
                            <input v-model="title" type="text" id="title"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="Recipe title" required/>
                        </div>

                        <div class="mb-4">
                            <label for="description"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Description</label>
                            <textarea v-model="description" id="description" rows="3"
                                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                      placeholder="Brief description of your recipe"></textarea>
                        </div>

                        <div class="mb-4">
                            <div class="flex flex-col sm:flex-row sm:space-x-6">
                                <!-- Preparation Time -->
                                <div class="sm:flex-1">
                                    <label for="preparationTime"
                                           class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Preparation
                                        time</label>
                                    <div class="flex items-center space-x-2">
                                        <select v-model="preparationTime" id="preparationTime"
                                                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500">
                                            <option
                                                v-for="(opt, idx) in Object.values(PreparationTime) as PreparationTime[]"
                                                :key="idx" :value="opt">{{ opt }}
                                            </option>
                                        </select>
                                    </div>
                                </div>

                                <!-- Number of Servings -->
                                <div class="mt-4 sm:mt-0 sm:flex-1">
                                    <label for="numServings"
                                           class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">No. of
                                        servings</label>
                                    <div class="flex items-center space-x-2">
                                        <input v-model="numServings" type="number" id="numServings" min="1" max="100"
                                               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 w-16"
                                               placeholder="4">
                                        <span class="text-sm text-gray-700 dark:text-gray-300">servings</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Ingredients Section -->
                    <div class="mb-8">
                        <div class="mb-4 flex items-center justify-between">
                            <h2 class="text-xl font-semibold text-gray-900">Ingredients</h2>
                            <!-- Mobile only reorder button -->
                            <button
                                v-if="isMobile"
                                type="button"
                                @click="toggleIngredientsReorder"
                                :class="[
                                    'flex items-center gap-2 px-3 py-1.5 rounded-md text-sm font-medium transition-colors',
                                    ingredientsReorderMode 
                                        ? 'bg-green-100 text-green-700 border border-green-300' 
                                        : 'bg-gray-100 text-gray-700 border border-gray-300 hover:bg-gray-200'
                                ]">
                                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16"/>
                                </svg>
                                {{ ingredientsReorderMode ? 'Done' : 'Order' }}
                            </button>
                        </div>

                        <VueDraggableNext 
                            v-model="ingredients" 
                            @end="onIngredientDragEnd"
                            handle=".ingredient-drag-handle"
                            :disabled="isMobile && !ingredientsReorderMode"
                            ghost-class="drag-ghost"
                            chosen-class="drag-chosen"
                            drag-class="drag-dragging">
                            <div v-for="(ingredient, index) in ingredients" :key="index" :class="['mb-3', isMobile && !ingredientsReorderMode ? '' : 'flex items-center']">
                                <!-- Drag handle - only for non-last items and only when visible -->
                                <div 
                                    v-if="index !== ingredients.length - 1 && (!isMobile || ingredientsReorderMode)"
                                    class="ingredient-drag-handle flex-shrink-0 w-5 mx-2 cursor-grab active:cursor-grabbing opacity-60 hover:opacity-100">
                                    <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16"/>
                                    </svg>
                                </div>
                                <!-- Empty space placeholder for last item when drag handles are visible -->
                                <div 
                                    v-if="index === ingredients.length - 1 && (!isMobile || ingredientsReorderMode)"
                                    class="flex-shrink-0 w-5 mx-2">
                                </div>
                                
                                <div class="relative flex-1">
                                    <textarea
                                        :value="ingredient.description"
                                        @input="onIngredientInput(index, ($event.target as HTMLTextAreaElement).value, $event)"
                                        rows="1"
                                        class="ingredient-textarea bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 pr-10 resize-none overflow-hidden min-h-[44px] dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                        placeholder="Add ingredient. Example: 3 tomatoes (diced)"></textarea>
                                    <button
                                        v-if="ingredients.length > 1 && index !== ingredients.length - 1"
                                        type="button"
                                        @click="removeIngredient(index)"
                                        class="absolute right-2 top-2 rounded-md p-1 text-gray-400 hover:text-red-500 focus:outline-none">
                                        <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </VueDraggableNext>
                    </div>

                    <!-- Preparation Steps Section -->
                    <div>
                        <div class="mb-4 flex items-center justify-between">
                            <h2 class="text-xl font-semibold text-gray-900">Instructions</h2>
                            <!-- Mobile only reorder button -->
                            <button
                                v-if="isMobile"
                                type="button"
                                @click="toggleStepsReorder"
                                :class="[
                                    'flex items-center gap-2 px-3 py-1.5 rounded-md text-sm font-medium transition-colors',
                                    stepsReorderMode 
                                        ? 'bg-green-100 text-green-700 border border-green-300' 
                                        : 'bg-gray-100 text-gray-700 border border-gray-300 hover:bg-gray-200'
                                ]">
                                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16"/>
                                </svg>
                                {{ stepsReorderMode ? 'Done' : 'Order' }}
                            </button>
                        </div>

                        <VueDraggableNext 
                            v-model="preparationSteps" 
                            @end="onStepDragEnd"
                            handle=".step-drag-handle"
                            :disabled="isMobile && !stepsReorderMode"
                            ghost-class="drag-ghost"
                            chosen-class="drag-chosen"
                            drag-class="drag-dragging">
                            <div v-for="(step, index) in preparationSteps" :key="index" :class="['mb-3', isMobile && !stepsReorderMode ? '' : 'flex items-center']">
                                <!-- Drag handle - only for non-last items and only when visible -->
                                <div 
                                    v-if="index !== preparationSteps.length - 1 && (!isMobile || stepsReorderMode)"
                                    class="step-drag-handle flex-shrink-0 w-5 mx-2 cursor-grab active:cursor-grabbing opacity-60 hover:opacity-100">
                                    <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16"/>
                                    </svg>
                                </div>
                                <!-- Empty space placeholder for last item when drag handles are visible -->
                                <div 
                                    v-if="index === preparationSteps.length - 1 && (!isMobile || stepsReorderMode)"
                                    class="flex-shrink-0 w-5 mx-2">
                                </div>
                                
                                <div class="relative flex-1">
                                    <textarea
                                        :value="step.description"
                                        @input="onStepInput(index, ($event.target as HTMLTextAreaElement).value, $event)"
                                        rows="1"
                                        class="step-textarea bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 pr-10 resize-none overflow-hidden min-h-[44px] dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                        placeholder="Add instruction step. Example: simmer the tomatoes"></textarea>
                                    <button
                                        v-if="preparationSteps.length > 1 && index !== preparationSteps.length - 1"
                                        type="button"
                                        @click="removeStep(index)"
                                        class="absolute right-2 top-2 rounded-md p-1 text-gray-400 hover:text-red-500 focus:outline-none">
                                        <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </VueDraggableNext>
                    </div>
                </div>

                <!-- Form Actions -->
                <div class="bg-gray-50 px-6 py-4">
                    <div class="flex justify-end space-x-3">
                        <button type="button" @click="cancelEdit"
                                class="rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2">
                            Cancel
                        </button>
                        <button type="submit"
                                class="rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                                :disabled="isSubmitting">
                            {{ isSubmitting ? 'Saving recipe...' : isEditMode ? 'Update Recipe' : 'Create Recipe' }}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
.drag-ghost {
    opacity: 0.4;
    background: #f3f4f6;
    border: 2px dashed #9ca3af;
    border-radius: 8px;
}

.drag-chosen {
    transform: scale(1.02);
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    border-radius: 8px;
}

.drag-dragging {
    opacity: 0.9;
    transform: rotate(1deg);
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
    border-radius: 8px;
}

/* Drag handle styles - no background, just lines */
.ingredient-drag-handle,
.step-drag-handle {
    transition: opacity 0.2s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 20px;
    height: 20px;
}

.ingredient-drag-handle:hover,
.step-drag-handle:hover {
    opacity: 1 !important;
}

/* Desktop hover behavior - show drag handles on hover */
@media (min-width: 768px) {
    .ingredient-drag-handle,
    .step-drag-handle {
        opacity: 0.6;
    }
    
    .ingredient-drag-handle:hover,
    .step-drag-handle:hover {
        opacity: 1;
    }
}

/* Ensure textareas don't interfere with drag handle */
.ingredient-textarea:focus,
.step-textarea:focus {
    z-index: 20;
}

/* Smooth transitions for drag operations */
* {
    transition: transform 0.2s ease, opacity 0.2s ease;
}

/* Reorder button animations */
button {
    transition: all 0.2s ease;
}

/* Mobile-specific adjustments */
@media (max-width: 767px) {
    .drag-ghost,
    .drag-chosen,
    .drag-dragging {
        margin: 0 -4px;
        padding: 0 4px;
    }
}
</style>
