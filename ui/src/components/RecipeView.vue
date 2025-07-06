<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
            <!-- Back Button -->
            <div class="mb-6">
                <button
                    @click="$router.push('/')"
                    class="inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50"
                >
                    <svg
                        class="mr-2 h-4 w-4"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                    >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 19l-7-7 7-7"
                        />
                    </svg>
                    Back to Recipes
                </button>
            </div>

            <!-- Loading State -->
            <div v-if="isLoading" class="py-12 text-center">
                <div class="mb-4 text-6xl">⏳</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">
                    Loading recipe...
                </h3>
                <p class="text-gray-600">
                    Please wait while we fetch the recipe details.
                </p>
            </div>

            <!-- Error State -->
            <div v-else-if="error" class="py-12 text-center">
                <div class="mb-4 text-6xl">❌</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">Error</h3>
                <p class="text-gray-600">{{ error }}</p>
                <button
                    @click="fetchRecipe"
                    class="mt-4 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white transition-colors duration-200 hover:bg-blue-700"
                >
                    Try Again
                </button>
            </div>

            <!-- Recipe Not Found -->
            <div v-else-if="!recipe" class="py-12 text-center">
                <div class="mb-4 text-6xl">🔍</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">
                    Recipe not found
                </h3>
                <p class="text-gray-600">
                    The recipe you're looking for doesn't exist.
                </p>
            </div>

            <!-- Recipe Content -->
            <div v-else class="overflow-hidden rounded-lg bg-white shadow-lg">
                <!-- Recipe Header -->
                <div class="border-b border-gray-200 px-6 py-8">
                    <h1 class="mb-4 text-3xl font-bold text-gray-900">
                        {{ recipe.title }}
                    </h1>
                    <p class="mb-4 text-lg text-gray-600">
                        {{ recipe.description }}
                    </p>
                    <div class="flex items-center text-sm text-gray-500">
                        <span class="mr-1">By </span>
                        <span class="font-medium">{{ recipe.author }}</span>
                    </div>
                </div>

                <div class="grid grid-cols-1 gap-8 p-6 lg:grid-cols-2">
                    <!-- Ingredients Section -->
                    <div>
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">
                            Ingredients
                        </h2>
                        <ul class="space-y-2">
                            <li
                                v-for="ingredient in sortedIngredients"
                                :key="ingredient.id"
                                class="flex items-start"
                            >
                                <span
                                    class="mt-1 mr-3 h-2 w-2 flex-shrink-0 rounded-full bg-blue-600"
                                ></span>
                                <div>
                                    <span class="font-medium text-gray-900">{{
                                        ingredient.quantity
                                    }}</span>
                                    <span class="ml-2 text-gray-700">{{
                                        ingredient.name
                                    }}</span>
                                </div>
                            </li>
                        </ul>
                    </div>

                    <!-- Instructions Section -->
                    <div>
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">
                            Instructions
                        </h2>
                        <ol class="space-y-4">
                            <li
                                v-for="step in sortedSteps"
                                :key="step.id"
                                class="flex items-start"
                            >
                                <span
                                    class="mr-3 flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-blue-600 text-xs font-medium text-white"
                                >
                                    {{ step.position }}
                                </span>
                                <p class="leading-relaxed text-gray-700">
                                    {{ step.description }}
                                </p>
                            </li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getRecipeById } from "../api/recipes";
import type { Recipe } from "../models/Recipe.ts";

const route = useRoute();
const recipe = ref<Recipe | null>(null);
const isLoading = ref<boolean>(true);
const error = ref<string | null>(null);

const sortedIngredients = computed(() => {
    if (!recipe.value) return [];
    return [...recipe.value.ingredients].sort(
        (a, b) => a.position - b.position,
    );
});

const sortedSteps = computed(() => {
    if (!recipe.value) return [];
    return [...recipe.value.steps].sort((a, b) => a.position - b.position);
});

const fetchRecipe = async () => {
    try {
        isLoading.value = true;
        const recipeId = parseInt(route.params.id as string);
        recipe.value = await getRecipeById(recipeId);
        error.value = null;
    } catch (err) {
        console.error("Failed to load recipe:", err);
        error.value = "Failed to load recipe. Please try again later.";
        recipe.value = null;
    } finally {
        isLoading.value = false;
    }
};

onMounted(() => {
    fetchRecipe();
});
</script>
