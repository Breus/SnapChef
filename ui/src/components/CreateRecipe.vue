<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

import { createRecipe } from "../services/recipes.ts";

const router = useRouter();

// Form data
const title = ref("");
const description = ref("");
const author = ref("");
const ingredients = ref([{ name: "", quantity: "" }]);
const steps = ref([{ description: "" }]);

// Form state
const isSubmitting = ref(false);
const error = ref<string | null>(null);

// Add a new ingredient field
const addIngredient = () => {
    ingredients.value.push({ name: "", quantity: "" });
};

// Remove an ingredient field
const removeIngredient = (index: number) => {
    if (ingredients.value.length > 1) {
        ingredients.value.splice(index, 1);
    }
};

// Add a new step field
const addStep = () => {
    steps.value.push({ description: "" });
};

// Remove a step field
const removeStep = (index: number) => {
    if (steps.value.length > 1) {
        steps.value.splice(index, 1);
    }
};

// Submit the form
const submitForm = async () => {
    try {
        isSubmitting.value = true;
        error.value = null;

        // Validate form
        if (!title.value.trim()) {
            throw new Error("Title is required");
        }
        if (!author.value.trim()) {
            throw new Error("Author is required");
        }
        if (ingredients.value.some((ing) => !ing.name.trim() || !ing.quantity.trim())) {
            throw new Error("All ingredient fields must be filled");
        }
        if (steps.value.some((step) => !step.description.trim())) {
            throw new Error("All step fields must be filled");
        }

        // Create recipe object
        const newRecipe = {
            title: title.value,
            description: description.value,
            author: author.value,
            ingredients: ingredients.value,
            steps: steps.value,
        };

        // Submit to API
        const createdRecipe = await createRecipe(newRecipe);

        // Navigate to the new recipe
        router.push(`/recipe/${createdRecipe.id}`);
    } catch (err) {
        if (err instanceof Error) {
            error.value = err.message;
        } else {
            error.value = "An unexpected error occurred";
        }
        console.error("Error creating recipe:", err);
    } finally {
        isSubmitting.value = false;
    }
};
</script>

<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
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
                <h1 class="text-3xl font-bold text-gray-900">Create New Recipe</h1>
                <p class="mt-2 text-gray-600">Fill in the details to create your recipe</p>
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
                <div class="p-6">
                    <!-- Basic Info Section -->
                    <div class="mb-8">
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">Basic Information</h2>

                        <div class="mb-4">
                            <label for="title" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Title</label>
                            <input v-model="title" type="text" id="title"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                                   placeholder="Recipe title" required />
                        </div>

                        <div class="mb-4">
                            <label for="description" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Description</label>
                            <textarea v-model="description" id="description" rows="3"
                                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                                      placeholder="Brief description of your recipe"></textarea>
                        </div>

                        <div>
                            <label for="author" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Author</label>
                            <input v-model="author" type="text" id="author"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                                   placeholder="Your name" required />
                        </div>
                    </div>

                    <!-- Ingredients Section -->
                    <div class="mb-8">
                        <div class="mb-4 flex items-center justify-between">
                            <h2 class="text-xl font-semibold text-gray-900">Ingredients</h2>
                            <button type="button" @click="addIngredient"
                                    class="inline-flex items-center rounded-md bg-blue-50 px-3 py-1 text-sm font-medium text-blue-700 hover:bg-blue-100">
                                <svg class="mr-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                                </svg>
                                Add Ingredient
                            </button>
                        </div>

                        <div v-for="(ingredient, index) in ingredients" :key="index" class="mb-3 flex items-center space-x-2">
                            <div class="w-1/3">
                                <input v-model="ingredient.quantity" type="text"
                                       class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                                       placeholder="Quantity" required />
                            </div>
                            <div class="flex-1">
                                <input v-model="ingredient.name" type="text"
                                       class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                                       placeholder="Ingredient name" required />
                            </div>
                            <button type="button" @click="removeIngredient(index)"
                                    class="rounded-md p-1 text-gray-400 hover:text-red-500"
                                    :disabled="ingredients.length <= 1">
                                <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                </svg>
                            </button>
                        </div>
                    </div>

                    <!-- Steps Section -->
                    <div>
                        <div class="mb-4 flex items-center justify-between">
                            <h2 class="text-xl font-semibold text-gray-900">Instructions</h2>
                            <button type="button" @click="addStep"
                                    class="inline-flex items-center rounded-md bg-blue-50 px-3 py-1 text-sm font-medium text-blue-700 hover:bg-blue-100">
                                <svg class="mr-1 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                                </svg>
                                Add Step
                            </button>
                        </div>

                        <div v-for="(step, index) in steps" :key="index" class="mb-3 flex items-start space-x-2">
                            <div class="mt-2 flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-blue-600 text-xs font-medium text-white">
                                {{ index + 1 }}
                            </div>
                            <div class="flex-1">
                                <textarea v-model="step.description" rows="2"
                                          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                                          placeholder="Describe this step" required></textarea>
                            </div>
                            <button type="button" @click="removeStep(index)"
                                    class="mt-2 rounded-md p-1 text-gray-400 hover:text-red-500"
                                    :disabled="steps.length <= 1">
                                <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Form Actions -->
                <div class="bg-gray-50 px-6 py-4">
                    <div class="flex justify-end space-x-3">
                        <button type="button" @click="$router.push('/')"
                                class="rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                            Cancel
                        </button>
                        <button type="submit"
                                class="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                                :disabled="isSubmitting">
                            {{ isSubmitting ? 'Creating...' : 'Create Recipe' }}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
