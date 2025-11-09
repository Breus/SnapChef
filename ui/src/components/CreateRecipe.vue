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

// Auto-resize function for textareas
const autoResize = (element: HTMLTextAreaElement) => {
    element.style.height = 'auto';
    element.style.height = element.scrollHeight + 'px';
};

// Drag and drop state
const draggedIngredientIndex = ref<number | null>(null);
const draggedStepIndex = ref<number | null>(null);
const dragOverIngredientIndex = ref<number | null>(null);
const dragOverStepIndex = ref<number | null>(null);

// Touch/mobile drag state
const touchStartY = ref<number>(0);
const touchCurrentY = ref<number>(0);
const isTouchDragging = ref<boolean>(false);

// Debouncing timeouts to prevent flickering
const ingredientDragLeaveTimeout = ref<number | null>(null);
const stepDragLeaveTimeout = ref<number | null>(null);

// Reusable drag and drop utilities
const createDragHandlers = (
    itemsRef: typeof ingredients | typeof preparationSteps,
    draggedIndexRef: typeof draggedIngredientIndex | typeof draggedStepIndex,
    dragOverIndexRef: typeof dragOverIngredientIndex | typeof dragOverStepIndex,
    timeoutRef: typeof ingredientDragLeaveTimeout | typeof stepDragLeaveTimeout
) => {
    const validateIndices = (sourceIndex: number | null, targetIndex: number) => {
        if (sourceIndex === null || sourceIndex === targetIndex) return false;
        if (targetIndex === itemsRef.value.length - 1) return true; // Allow end position
        return sourceIndex >= 0 && sourceIndex < itemsRef.value.length - 1 &&
               targetIndex >= 0 && targetIndex < itemsRef.value.length - 1;
    };

    const performReorder = (sourceIndex: number, targetIndex: number) => {
        if (targetIndex === itemsRef.value.length - 1) {
            // Drop at end - insert before empty field
            const [draggedItem] = itemsRef.value.splice(sourceIndex, 1);
            if (draggedItem) {
                itemsRef.value.splice(itemsRef.value.length - 1, 0, draggedItem);
                nextTick(() => autoResizeAllTextareas());
            }
        } else {
            // Regular drop - calculate actual position after removal
            let actualTargetIndex = targetIndex;
            if (sourceIndex < targetIndex) {
                actualTargetIndex = targetIndex - 1;
            }

            if (sourceIndex !== actualTargetIndex) {
                const [draggedItem] = itemsRef.value.splice(sourceIndex, 1);
                if (draggedItem) {
                    itemsRef.value.splice(actualTargetIndex, 0, draggedItem);
                    nextTick(() => autoResizeAllTextareas());
                }
            }
        }
    };

    const handleDragOver = (targetIndex: number, event: DragEvent) => {
        event.preventDefault();
        if (event.dataTransfer) {
            event.dataTransfer.dropEffect = 'move';
        }

        const sourceIndex = draggedIndexRef.value;
        if (!validateIndices(sourceIndex, targetIndex)) {
            dragOverIndexRef.value = null;
            return;
        }

        dragOverIndexRef.value = targetIndex === itemsRef.value.length - 1 ?
            itemsRef.value.length - 1 : targetIndex;
    };

    const handleDragLeave = (event: DragEvent) => {
        if (timeoutRef.value) {
            clearTimeout(timeoutRef.value);
            timeoutRef.value = null;
        }

        const tolerance = 15;
        const rect = (event.currentTarget as HTMLElement).getBoundingClientRect();
        const x = event.clientX;
        const y = event.clientY;

        if (x < rect.left - tolerance || x > rect.right + tolerance ||
            y < rect.top - tolerance || y > rect.bottom + tolerance) {
            timeoutRef.value = window.setTimeout(() => {
                const currentRect = (event.currentTarget as HTMLElement)?.getBoundingClientRect();
                if (currentRect && (x < currentRect.left - tolerance || x > currentRect.right + tolerance ||
                    y < currentRect.top - tolerance || y > currentRect.bottom + tolerance)) {
                    dragOverIndexRef.value = null;
                }
                timeoutRef.value = null;
            }, 100);
        }
    };

    const handleDrop = (targetIndex: number, event: DragEvent) => {
        event.preventDefault();
        const sourceIndex = draggedIndexRef.value;

        if (sourceIndex !== null && sourceIndex !== targetIndex &&
            validateIndices(sourceIndex, targetIndex)) {
            performReorder(sourceIndex, targetIndex);
        }

        draggedIndexRef.value = null;
        dragOverIndexRef.value = null;
    };

    const handleDragEnd = () => {
        draggedIndexRef.value = null;
        dragOverIndexRef.value = null;
        if (timeoutRef.value) {
            clearTimeout(timeoutRef.value);
            timeoutRef.value = null;
        }
    };

    const handleTouchStart = (index: number, event: TouchEvent) => {
        if (index === itemsRef.value.length - 1 || !event.touches[0]) return;

        const target = event.target as HTMLElement;
        const deleteButton = target.closest('button');
        if (deleteButton && deleteButton.onclick) return;

        draggedIndexRef.value = index;
        isTouchDragging.value = true;
        touchStartY.value = event.touches[0].clientY;
        touchCurrentY.value = event.touches[0].clientY;
        event.preventDefault();
    };

    const handleTouchMove = (event: TouchEvent, dataAttribute: string) => {
        if (!isTouchDragging.value || draggedIndexRef.value === null || !event.touches[0]) return;

        touchCurrentY.value = event.touches[0].clientY;
        const elementBelow = document.elementFromPoint(event.touches[0].clientX, event.touches[0].clientY);
        const item = elementBelow?.closest(`[${dataAttribute}]`);

        if (item) {
            const targetIndex = parseInt(item.getAttribute(dataAttribute) || '-1');
            const sourceIndex = draggedIndexRef.value;

            if (!validateIndices(sourceIndex, targetIndex)) {
                dragOverIndexRef.value = null;
                return;
            }

            dragOverIndexRef.value = targetIndex === itemsRef.value.length - 1 ?
                itemsRef.value.length - 1 : targetIndex;
        } else {
            dragOverIndexRef.value = null;
        }

        event.preventDefault();
    };

    const handleTouchEnd = (event: TouchEvent) => {
        if (!isTouchDragging.value || draggedIndexRef.value === null) return;

        const sourceIndex = draggedIndexRef.value;
        const targetIndex = dragOverIndexRef.value;

        if (sourceIndex !== null && targetIndex !== null && sourceIndex !== targetIndex &&
            validateIndices(sourceIndex, targetIndex)) {
            performReorder(sourceIndex, targetIndex);
        }

        draggedIndexRef.value = null;
        dragOverIndexRef.value = null;
        isTouchDragging.value = false;
        touchStartY.value = 0;
        touchCurrentY.value = 0;
        event.preventDefault();
    };

    return {
        handleDragOver,
        handleDragLeave,
        handleDrop,
        handleDragEnd,
        handleTouchStart,
        handleTouchMove,
        handleTouchEnd
    };
};

// Create ingredient drag handlers
const ingredientHandlers = createDragHandlers(
    ingredients,
    draggedIngredientIndex,
    dragOverIngredientIndex,
    ingredientDragLeaveTimeout
);

// Create step drag handlers
const stepHandlers = createDragHandlers(
    preparationSteps,
    draggedStepIndex,
    dragOverStepIndex,
    stepDragLeaveTimeout
);

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
        // Auto-resize the new field after it's added to the DOM
        nextTick(() => {
            const textareas = document.querySelectorAll('.ingredient-textarea');
            const newTextarea = textareas[textareas.length - 1] as HTMLTextAreaElement;
            if (newTextarea) {
                autoResize(newTextarea);
            }
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

// Ingredient drag handlers using reusable utilities
const onIngredientDragStart = (index: number, event: DragEvent) => {
    draggedIngredientIndex.value = index;
    if (event.dataTransfer) {
        event.dataTransfer.effectAllowed = 'move';
        event.dataTransfer.setData('text/plain', index.toString());
    }
};

const onIngredientDragOver = ingredientHandlers.handleDragOver;
const onIngredientDragLeave = ingredientHandlers.handleDragLeave;
const onIngredientDrop = ingredientHandlers.handleDrop;
const onIngredientDragEnd = ingredientHandlers.handleDragEnd;
const onIngredientTouchStart = ingredientHandlers.handleTouchStart;
const onIngredientTouchMove = (event: TouchEvent) => ingredientHandlers.handleTouchMove(event, 'data-ingredient-index');
const onIngredientTouchEnd = ingredientHandlers.handleTouchEnd;

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
        // Auto-resize the new field after it's added to the DOM
        nextTick(() => {
            const textareas = document.querySelectorAll('.step-textarea');
            const newTextarea = textareas[textareas.length - 1] as HTMLTextAreaElement;
            if (newTextarea) {
                autoResize(newTextarea);
            }
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

// Step drag handlers using reusable utilities
const onStepDragStart = (index: number, event: DragEvent) => {
    draggedStepIndex.value = index;
    if (event.dataTransfer) {
        event.dataTransfer.effectAllowed = 'move';
        event.dataTransfer.setData('text/plain', index.toString());
    }
};

const onStepDragOver = stepHandlers.handleDragOver;
const onStepDragLeave = stepHandlers.handleDragLeave;
const onStepDrop = stepHandlers.handleDrop;
const onStepDragEnd = stepHandlers.handleDragEnd;
const onStepTouchStart = stepHandlers.handleTouchStart;
const onStepTouchMove = (event: TouchEvent) => stepHandlers.handleTouchMove(event, 'data-step-index');
const onStepTouchEnd = stepHandlers.handleTouchEnd;

// Auto-resize all textareas in the form
const autoResizeAllTextareas = () => {
    nextTick(() => {
        const allTextareas = document.querySelectorAll('.ingredient-textarea, .step-textarea, #description');
        allTextareas.forEach(textarea => {
            autoResize(textarea as HTMLTextAreaElement);
        });
    });
};

onMounted(async () => {
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
            autoResizeAllTextareas();
        } catch (err) {
            error.value = "Failed to load recipe for editing.";
        }
    } else {
        // Auto-resize textareas on initial mount for new recipes
        autoResizeAllTextareas();
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
                            <textarea v-model="description" id="description" rows="2"
                                      @input="autoResize($event.target as HTMLTextAreaElement)"
                                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 resize-none overflow-hidden"
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
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">Ingredients</h2>

                        <div v-for="(ingredient, index) in ingredients" :key="index" class="relative">
                            <!-- Drop Zone Above -->
                            <div
                                v-if="draggedIngredientIndex !== null && draggedIngredientIndex !== index && index !== ingredients.length - 1 && dragOverIngredientIndex === index"
                                class="h-2 bg-green-200 border-2 border-green-400 border-dashed rounded-md mb-2 flex items-center justify-center">
                                <div class="w-8 h-0.5 bg-green-600 rounded"></div>
                            </div>

                            <div
                                class="mb-3 relative flex items-start group transition-all duration-200"
                                :class="{
                                    'opacity-50 scale-95': draggedIngredientIndex === index,
                                    'transform translate-y-2': draggedIngredientIndex !== null && draggedIngredientIndex !== index && dragOverIngredientIndex !== null && index > dragOverIngredientIndex && index !== ingredients.length - 1,
                                    'transform -translate-y-2': draggedIngredientIndex !== null && draggedIngredientIndex !== index && dragOverIngredientIndex !== null && index < dragOverIngredientIndex
                                }"
                                :data-ingredient-index="index"
                                :draggable="index !== ingredients.length - 1"
                                @dragstart="onIngredientDragStart(index, $event)"
                                @dragover="onIngredientDragOver(index, $event)"
                                @dragleave="onIngredientDragLeave"
                                @drop="onIngredientDrop(index, $event)"
                                @dragend="onIngredientDragEnd"
                                @touchstart="onIngredientTouchStart(index, $event)"
                                @touchmove="onIngredientTouchMove"
                                @touchend="onIngredientTouchEnd">

                                <!-- Drag Handle or Spacer -->
                                <div class="flex-shrink-0 mr-3 mt-2.5">
                                    <div
                                        v-if="index !== ingredients.length - 1 && ingredients.length > 1"
                                        class="opacity-60 cursor-move touch-none select-none"
                                        title="Drag to reorder">
                                        <svg class="w-4 h-4 text-gray-500" fill="currentColor" viewBox="0 0 24 24">
                                            <path d="M9 5h2v2H9V5zm0 6h2v2H9v-2zm0 6h2v2H9v-2zm6-12h2v2h-2V5zm0 6h2v2h-2v-2zm0 6h2v2h-2v-2z"/>
                                        </svg>
                                    </div>
                                    <!-- Invisible spacer for alignment when no drag handle -->
                                    <div v-else class="w-4 h-4"></div>
                                </div>

                                <div class="flex-1 relative">
                                    <textarea
                                        :value="ingredient.description"
                                        @input="onIngredientInput(index, ($event.target as HTMLTextAreaElement).value, $event)"
                                        rows="1"
                                        class="ingredient-textarea bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 pr-10 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 resize-none overflow-hidden"
                                        placeholder="Add ingredient. Example: 3 tomatoes (diced)"></textarea>
                                    <button
                                        v-if="ingredients.length > 1 && index !== ingredients.length - 1"
                                        type="button"
                                        @click="removeIngredient(index)"
                                        @touchstart.stop
                                        @touchend.stop
                                        class="absolute right-1 top-1 rounded-md p-2 text-gray-400 hover:text-red-500 focus:outline-none touch-manipulation">
                                        <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>

                            <!-- Drop Zone Below (for dropping at the end) -->
                            <div
                                v-if="draggedIngredientIndex !== null && index === ingredients.length - 2 && dragOverIngredientIndex === ingredients.length - 1"
                                class="h-2 bg-green-200 border-2 border-green-400 border-dashed rounded-md mb-2 flex items-center justify-center">
                                <div class="w-8 h-0.5 bg-green-600 rounded"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Preparation Steps Section -->
                    <div>
                        <div class="mb-4 flex items-center justify-between">
                            <h2 class="text-xl font-semibold text-gray-900">Instructions</h2>
                        </div>

                        <div v-for="(step, index) in preparationSteps" :key="index" class="relative">
                            <!-- Drop Zone Above -->
                            <div
                                v-if="draggedStepIndex !== null && draggedStepIndex !== index && index !== preparationSteps.length - 1 && dragOverStepIndex === index"
                                class="h-2 bg-green-200 border-2 border-green-400 border-dashed rounded-md mb-2 flex items-center justify-center">
                                <div class="w-8 h-0.5 bg-green-600 rounded"></div>
                            </div>

                            <div
                                class="mb-3 relative flex items-start group transition-all duration-200"
                                :class="{
                                    'opacity-50 scale-95': draggedStepIndex === index,
                                    'transform translate-y-2': draggedStepIndex !== null && draggedStepIndex !== index && dragOverStepIndex !== null && index > dragOverStepIndex && index !== preparationSteps.length - 1,
                                    'transform -translate-y-2': draggedStepIndex !== null && draggedStepIndex !== index && dragOverStepIndex !== null && index < dragOverStepIndex
                                }"
                                :data-step-index="index"
                                :draggable="index !== preparationSteps.length - 1"
                                @dragstart="onStepDragStart(index, $event)"
                                @dragover="onStepDragOver(index, $event)"
                                @dragleave="onStepDragLeave"
                                @drop="onStepDrop(index, $event)"
                                @dragend="onStepDragEnd"
                                @touchstart="onStepTouchStart(index, $event)"
                                @touchmove="onStepTouchMove"
                                @touchend="onStepTouchEnd">


                                <!-- Drag Handle or Spacer -->
                                <div class="flex-shrink-0 mr-3 mt-2.5">
                                    <div
                                        v-if="index !== preparationSteps.length - 1 && preparationSteps.length > 1"
                                        class="opacity-60 cursor-move touch-none select-none"
                                        title="Drag to reorder">
                                        <svg class="w-4 h-4 text-gray-500" fill="currentColor" viewBox="0 0 24 24">
                                            <path d="M9 5h2v2H9V5zm0 6h2v2H9v-2zm0 6h2v2H9v-2zm6-12h2v2h-2V5zm0 6h2v2h-2v-2zm0 6h2v2h-2v-2z"/>
                                        </svg>
                                    </div>
                                    <!-- Invisible spacer for alignment when no drag handle -->
                                    <div v-else class="w-4 h-4"></div>
                                </div>

                                <div class="flex-1 relative">
                                    <textarea
                                        :value="step.description"
                                        @input="onStepInput(index, ($event.target as HTMLTextAreaElement).value, $event)"
                                        rows="1"
                                        class="step-textarea bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 pr-10 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500 resize-none overflow-hidden"
                                        placeholder="Add instruction step. Example: simmer the tomatoes"></textarea>
                                    <button
                                        v-if="preparationSteps.length > 1 && index !== preparationSteps.length - 1"
                                        type="button"
                                        @click="removeStep(index)"
                                        @touchstart.stop
                                        @touchend.stop
                                        class="absolute right-1 top-1 rounded-md p-2 text-gray-400 hover:text-red-500 focus:outline-none touch-manipulation">
                                        <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>

                            <!-- Drop Zone Below (for dropping at the end) -->
                            <div
                                v-if="draggedStepIndex !== null && index === preparationSteps.length - 2 && dragOverStepIndex === preparationSteps.length - 1"
                                class="h-2 bg-green-200 border-2 border-green-400 border-dashed rounded-md mb-2 flex items-center justify-center">
                                <div class="w-8 h-0.5 bg-green-600 rounded"></div>
                            </div>
                        </div>
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
