<style>
.trash-icon {
    filter: grayscale(1) brightness(2);
    transition: filter 0.15s;
}

.group:hover .trash-icon {
    filter: none;
}
</style>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { getRecipeById } from "../api/recipeApi.ts";
import { useAuth } from "../auth/useAuth.ts";
import type Recipe from "../models/domain/Recipe.ts";

const route = useRoute();
const recipe = ref<Recipe | null>(null);
const isLoading = ref<boolean>(true);
const error = ref<string | null>(null);
const { userId } = useAuth();

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

<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
            <!-- Back Button -->
            <div class="mb-6">
                <button @click="$router.push('/')"
                    class="inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50">
                    <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
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
                <button @click="fetchRecipe"
                    class="mt-4 rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white transition-colors duration-200 hover:bg-green-700">
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
                <div class="relative border-b border-gray-300 px-6 py-8 group">
                    <button v-if="recipe.author.userId === userId" @click="deleteRecipe"
                        class="absolute top-6 right-6 p-0 bg-transparent shadow-none hover:bg-transparent"
                        title="Delete Recipe">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                            stroke="currentColor"
                            class="w-6 h-6 text-gray-400 hover:text-black transition-colors duration-150 cursor-pointer">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
                        </svg>
                    </button>
                    <h1 class="mb-4 text-3xl font-bold text-gray-900">
                        {{ recipe.title }}
                    </h1>
                    <p class="mb-4 text-lg text-gray-600">
                        {{ recipe.description }}
                    </p>
                    <div class="flex items-center text-sm text-gray-500">
                        <span class="mr-1">By </span>
                        <span v-if="recipe.author.userId === userId" class="font-medium">{{ recipe.author.userName }}
                            (Me)</span>
                        <span v-else class="font-medium">{{ recipe.author.userName }}</span>
                    </div>
                </div>

                <div class="grid grid-cols-1 gap-8 p-6 lg:grid-cols-2">
                    <!-- Ingredients Section -->
                    <div>
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">
                            Ingredients
                        </h2>
                        <ul class="space-y-2">
                            <li v-for="ingredient in recipe.ingredients" :key="ingredient.id" class="flex items-start">
                                <span class="mt-1 mr-3 h-2 w-2 flex-shrink-0 rounded-full bg-green-600"></span>
                                <div>
                                    <span class="font-medium text-gray-900">{{ ingredient.quantity }}</span>
                                    <span class="ml-2 text-gray-700">{{ ingredient.name }}</span>
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
                            <li v-for="(step, number) in recipe.steps" :key="step.id" class="flex items-start">
                                <span
                                    class="mr-3 flex h-6 w-6 flex-shrink-0 items-center justify-center rounded-full bg-green-600 text-xs font-medium text-white">
                                    {{ number + 1 }}
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
