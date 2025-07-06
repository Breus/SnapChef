<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
      <!-- Back Button -->
      <div class="mb-6">
        <button
          @click="$router.push('/')"
          class="inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50"
        >
          <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          Back to Recipes
        </button>
      </div>

      <!-- Recipe Not Found -->
      <div v-if="!recipe" class="py-12 text-center">
        <div class="mb-4 text-6xl">🔍</div>
        <h3 class="mb-2 text-xl font-medium text-gray-900">Recipe not found</h3>
        <p class="text-gray-600">The recipe you're looking for doesn't exist.</p>
      </div>

      <!-- Recipe Content -->
      <div v-else class="bg-white rounded-lg shadow-lg overflow-hidden">
        <!-- Recipe Header -->
        <div class="px-6 py-8 border-b border-gray-200">
          <h1 class="mb-4 text-3xl font-bold text-gray-900">{{ recipe.title }}</h1>
          <p class="mb-4 text-lg text-gray-600">{{ recipe.description }}</p>
          <div class="flex items-center text-sm text-gray-500">
            <span class="mr-1">By </span>
            <span class="font-medium">{{ getAuthorName(recipe.author_id) }}</span>
          </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 p-6">
          <!-- Ingredients Section -->
          <div>
            <h2 class="mb-4 text-xl font-semibold text-gray-900">Ingredients</h2>
            <ul class="space-y-2">
              <li
                v-for="ingredient in sortedIngredients"
                :key="ingredient.id"
                class="flex items-start"
              >
                <span class="mr-3 mt-1 h-2 w-2 rounded-full bg-blue-600 flex-shrink-0"></span>
                <div>
                  <span class="font-medium text-gray-900">{{ ingredient.quantity }}</span>
                  <span class="ml-2 text-gray-700">{{ ingredient.name }}</span>
                </div>
              </li>
            </ul>
          </div>

          <!-- Instructions Section -->
          <div>
            <h2 class="mb-4 text-xl font-semibold text-gray-900">Instructions</h2>
            <ol class="space-y-4">
              <li
                v-for="step in sortedSteps"
                :key="step.id"
                class="flex items-start"
              >
                <span class="mr-3 flex h-6 w-6 items-center justify-center rounded-full bg-blue-600 text-xs font-medium text-white flex-shrink-0">
                  {{ step.position }}
                </span>
                <p class="text-gray-700 leading-relaxed">{{ step.description }}</p>
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
import { mockRecipes, getUserById, type Recipe } from "../data/mockRecipes";

const route = useRoute();
const recipe = ref<Recipe | null>(null);

const sortedIngredients = computed(() => {
  if (!recipe.value) return [];
  return [...recipe.value.ingredients].sort((a, b) => a.position - b.position);
});

const sortedSteps = computed(() => {
  if (!recipe.value) return [];
  return [...recipe.value.steps].sort((a, b) => a.position - b.position);
});

const getAuthorName = (authorId: number): string => {
  const user = getUserById(authorId);
  return user ? user.username : "Unknown Chef";
};

onMounted(() => {
  const recipeId = parseInt(route.params.id as string);
  recipe.value = mockRecipes.find(r => r.id === recipeId) || null;
});
</script>