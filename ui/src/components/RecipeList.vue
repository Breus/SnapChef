<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
      <!-- Header -->
      <div class="mb-12 text-center">
        <h1 class="mb-4 text-4xl font-bold text-gray-900">Recipe Book</h1>
        <!--        <p class="text-lg text-gray-600">-->
        <!--          Discover delicious recipes from our community-->
        <!--        </p>-->
      </div>

      <!-- Search Bar -->
      <div class="mx-auto mb-8 max-w-md">
        <div class="relative">
          <div
              class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3"
          >
            <svg
                class="h-5 w-5 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
              <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              ></path>
            </svg>
          </div>
          <input
              v-model="searchQuery"
              type="text"
              placeholder="Search recipes, ingredients, or authors..."
              class="block w-full rounded-md border border-gray-300 bg-white py-2 pr-3 pl-10 leading-5 placeholder-gray-500 focus:border-blue-500 focus:placeholder-gray-400 focus:ring-1 focus:ring-blue-500 focus:outline-none"
          />
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading" class="py-12 text-center">
        <div class="mb-4 text-6xl">⏳</div>
        <h3 class="mb-2 text-xl font-medium text-gray-900">Loading recipes...</h3>
        <p class="text-gray-600">Please wait while we fetch the recipes.</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="py-12 text-center">
        <div class="mb-4 text-6xl">❌</div>
        <h3 class="mb-2 text-xl font-medium text-gray-900">Error</h3>
        <p class="text-gray-600">{{ error }}</p>
        <button
            @click="fetchRecipes"
            class="mt-4 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white transition-colors duration-200 hover:bg-blue-700"
        >
          Try Again
        </button>
      </div>

      <!-- Recipe Grid -->
      <div v-else-if="recipes.length > 0" class="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
        <div
            v-for="recipe in recipes"
            :key="recipe.id"
            class="flex h-full flex-col overflow-hidden rounded-lg bg-white shadow-md transition-shadow duration-300 hover:shadow-lg"
        >
          <!-- Recipe Content -->
          <div class="flex flex-1 flex-col p-6">
            <h3 class="mb-2 text-xl font-semibold text-gray-900">
              {{ recipe.title }}
            </h3>
            <p class="mb-4 line-clamp-3 text-sm text-gray-600">
              {{ recipe.description }}
            </p>

            <!-- Recipe Meta -->
            <div
                class="mb-4 flex items-center justify-start text-sm text-gray-500"
            >
              <div class="flex items-center">
                <span class="mr-1">By </span>
                <span>{{ recipe.author }}</span>
              </div>
            </div>

            <!-- Ingredients Preview -->
            <div class="mb-4">
              <h4 class="mb-2 text-sm font-medium text-gray-700">
                Ingredients:
              </h4>
              <div class="flex flex-wrap gap-1">
                <span
                    v-for="ingredient in recipe.ingredients.slice(0, 3)"
                    :key="ingredient.id"
                    class="inline-block rounded-full bg-gray-100 px-2 py-1 text-xs text-gray-700"
                >
                  {{ ingredient.name }}
                </span>
                <span
                    v-if="recipe.ingredients.length > 3"
                    class="inline-block rounded-full bg-gray-200 px-2 py-1 text-xs text-gray-600"
                >
                  +{{ recipe.ingredients.length - 3 }} more
                </span>
              </div>
            </div>

            <!-- View Recipe Button -->
            <div class="mt-auto flex items-center justify-end">
              <button
                  @click="$router.push(`/recipe/${recipe.id}`)"
                  class="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white transition-colors duration-200 hover:bg-blue-700"
              >
                View Recipe
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else class="py-12 text-center">
        <div class="mb-4 text-6xl">🍳</div>
        <h3 class="mb-2 text-xl font-medium text-gray-900">No recipes found</h3>
        <p class="text-gray-600">Start by adding your first recipe!</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {getAllRecipes} from "../api/recipes";
import type {Recipe} from "../models/Recipe.ts";

const allRecipes = ref<Recipe[]>([]);
const searchQuery = ref<string>("");
const isLoading = ref<boolean>(true);
const error = ref<string | null>(null);

const recipes = computed(() => {
  if (!searchQuery.value.trim()) {
    return allRecipes.value;
  }

  const query = searchQuery.value.toLowerCase().trim();

  return allRecipes.value.filter((recipe) => {
    // Search in title
    if (recipe.title.toLowerCase().includes(query)) {
      return true;
    }

    // Search in description
    if (recipe.description.toLowerCase().includes(query)) {
      return true;
    }

    // Search in author name
    const authorName = recipe.author.toLowerCase();
    if (authorName.includes(query)) {
      return true;
    }

    // Search in ingredient names
    return recipe.ingredients.some((ingredient) =>
        ingredient.name.toLowerCase().includes(query),
    );
  });
});

const fetchRecipes = async () => {
  try {
    isLoading.value = true;
    allRecipes.value = await getAllRecipes();
    error.value = null;
  } catch (err) {
    console.error("Failed to load recipes:", err);
    error.value = "Failed to load recipes. Please try again later.";
    allRecipes.value = [];
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  fetchRecipes();
});
</script>
