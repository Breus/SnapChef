<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { deleteRecipeById, getRecipeById } from "../api/recipeApi.ts";
import { addRecipeToUserFavorites, getUserFavoriteRecipesIds, removeRecipeFromUserFavorites } from "../api/userApi.ts";
import { useAuth } from "../auth/useAuth.ts";
import type Recipe from "../models/domain/Recipe.ts";

const route = useRoute();
const router = useRouter();
const recipe = ref<Recipe | null>(null);
const showDeleteModal = ref(false);
const isLoading = ref<boolean>(true);
const showLoading = ref<boolean>(false);
let loadingTimer: number | null = null;

const error = ref<string | null>(null);
const {authToken, userId} = useAuth();
const userFavorites = ref<number[]>([]);

const fetchRecipe = async () => {
    try {
        isLoading.value = true;
        if (loadingTimer) {
            clearTimeout(loadingTimer);
        }
        loadingTimer = setTimeout(() => {
            if (isLoading.value) {
                showLoading.value = true;
            }
        }, 300);
        const recipeId = parseInt(route.params.id as string);
        recipe.value = await getRecipeById(recipeId);
        error.value = null;
    } catch (err) {
        console.error("Failed to load recipe:", err);
        error.value = "Failed to load recipe. Please try again later.";
        recipe.value = null;
    } finally {
        isLoading.value = false;
        showLoading.value = false;
        if (loadingTimer) {
            clearTimeout(loadingTimer);
        }
    }
};

const editRecipe = () => {
    router.push(`/recipe/${recipe.value?.id}/edit`);
};

const deleteRecipe = async () => {
    showDeleteModal.value = true;
};

const confirmDelete = async () => {
    if (!recipe.value) return;
    try {
        if (!authToken || authToken.value === null) {
            alert("You must be logged in to delete a recipe.");
            return;
        }
        await deleteRecipeById(recipe.value.id, authToken.value);
        router.push("/");
    } catch (err) {
        alert("Failed to delete recipe.");
        console.error(err);
    } finally {
        showDeleteModal.value = false;
    }
};

const cancelDelete = () => {
    showDeleteModal.value = false;
};


const clickFavoriteRecipe = async () => {
    if (userFavorites.value.includes(recipe.value?.id as number)) {
        await unfavoriteRecipe(recipe.value?.id as number);
    } else {
        await favoriteRecipe(recipe.value?.id as number);
    }
}

const favoriteRecipe = async (recipeId: number) => {
    try {
        if (!authToken || authToken.value === null || !userId || userId.value === null) {
            router.push("/login");
            return;
        }
        userFavorites.value = await addRecipeToUserFavorites(userId.value, recipeId, authToken.value);
    } catch (err) {
        console.error("Failed to favorite recipe:", err);
        throw err;
    }
};

const unfavoriteRecipe = async (recipeId: number) => {
    try {
        if (!authToken || authToken.value === null || !userId || userId.value === null) {
            router.push("/login");
            return;
        }
        userFavorites.value = await removeRecipeFromUserFavorites(userId.value, recipeId, authToken.value);
    } catch (err) {
        console.error("Failed to unfavorite recipe:", err);
        throw err;
    }
}

const fetchUserFavorites = async () => {
    if (!userId || userId.value === null) {
        return;
    }
    try {
        if (!authToken || authToken.value === null) {
            return; // do nothing if a user is not logged in
        }
        userFavorites.value = await getUserFavoriteRecipesIds(userId.value, authToken.value);
    } catch (err) {
        console.error("Failed to fetch favorites:", err);
    }
};

onMounted(async () => {
    await fetchUserFavorites();
    await fetchRecipe();
});
</script>

<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
            <!-- Back Button -->
            <div class="mb-6">
                <button @click="$router.push('/')"
                        class="inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50 cursor-pointer transition-colors duration-150">
                    <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                    </svg>
                    Back to Recipes
                </button>
            </div>

            <!-- Loading State -->
            <div v-if="showLoading" class="py-12 text-center">
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
            </div>

            <!-- Recipe Not Found -->
            <div v-else-if="!isLoading && !recipe" class="py-12 text-center">
                <div class="mb-4 text-6xl">🔍</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">
                    Recipe not found
                </h3>
                <p class="text-gray-600">
                    The recipe you're looking for doesn't exist.
                </p>
            </div>

            <!--- Delete Recipe Modal -->
            <div v-if="showDeleteModal" class="fixed inset-0 z-50 flex items-center justify-center">
                <div class="bg-white rounded-lg shadow-lg p-8 max-w-sm w-full">
                    <h2 class="text-xl font-bold text-red-700 mb-4">Delete Recipe</h2>
                    <p class="mb-6 text-gray-700">
                        Are you sure you want to delete this recipe? This action cannot be undone.
                    </p>
                    <div class="flex justify-end space-x-3">
                        <button @click="cancelDelete"
                                class="cursor-pointer rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50">
                            Cancel
                        </button>
                        <button @click="confirmDelete"
                                class="cursor-pointer rounded-md bg-red-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-red-700">
                            Delete
                        </button>
                    </div>
                </div>
            </div>

            <!-- Recipe Content -->
            <div v-else-if="!showLoading && recipe" class="overflow-hidden rounded-lg bg-white shadow-lg">
                <!-- Recipe Header -->
                <div class="relative border-b border-gray-300 px-6 py-8 group">
                    <div class="absolute top-6 right-6 flex space-x-2">
                        <button @click="clickFavoriteRecipe" class="p-0 bg-transparent shadow-none hover:bg-transparent"
                                title="Favorite Recipe">
                            <svg v-if="recipe && userFavorites.includes(recipe.id)" xmlns="http://www.w3.org/2000/svg"
                                 :fill="'#16a34a'" stroke="none" viewBox="0 0 24 24" stroke-width="1.5"
                                 class="w-6 h-6 transition-colors duration-150 cursor-pointer">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"/>
                            </svg>
                            <svg v-else xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                 stroke-width="1.5" stroke="currentColor"
                                 class="w-6 h-6 text-gray-400 hover:text-green-600 transition-colors duration-150 cursor-pointer">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"/>
                            </svg>
                        </button>
                        <button v-if="recipe && recipe.author && recipe.author.userId == userId" @click="editRecipe"
                                class="p-0 bg-transparent shadow-none hover:bg-transparent" title="Edit Recipe">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                                 stroke="currentColor"
                                 class="w-6 h-6 text-gray-400 hover:text-black transition-colors duration-150 cursor-pointer">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"/>
                            </svg>
                        </button>
                        <button v-if="recipe && recipe.author && recipe.author.userId == userId" @click="deleteRecipe"
                                class="p-0 bg-transparent shadow-none hover:bg-transparent" title="Delete Recipe">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                                 stroke="currentColor"
                                 class="w-6 h-6 text-gray-400 hover:text-black transition-colors duration-150 cursor-pointer">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"/>
                            </svg>
                        </button>
                    </div>
                    <h1 class="mb-4 text-3xl font-bold text-gray-900">
                        {{ recipe?.title }}
                    </h1>
                    <p class="mb-4 text-lg text-gray-600">
                        {{ recipe?.description }}
                    </p>
                    <div class="flex items-center text-sm text-gray-500">
                        <span class="mr-1">By </span>
                        <span v-if="recipe && recipe.author && recipe.author.userId === userId" class="font-medium">
                            {{ recipe.author.userName }} (Me)
                        </span>
                        <span v-else-if="recipe && recipe.author" class="font-medium">
                            {{ recipe.author.userName }}
                        </span>
                    </div>
                </div>
                <!-- Recipe Body (Main section)-->
                <div class="grid grid-cols-1 gap-8 p-6 lg:grid-cols-2">
                    <!-- Ingredients Section -->
                    <div>
                        <h2 class="mb-4 text-xl font-semibold text-gray-900">
                            Ingredients
                        </h2>
                        <ul class="space-y-2">
                            <li v-for="ingredient in recipe?.ingredients || []" :key="ingredient.name"
                                class="flex items-start">
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
                            <li v-for="(step, number) in recipe?.preparationSteps || []" :key="step.description"
                                class="flex items-start">
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
