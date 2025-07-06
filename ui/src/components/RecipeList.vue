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

      <!-- Recipe Grid -->
      <div class="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
        <div
          v-for="recipe in recipes"
          :key="recipe.id"
          class="flex flex-col h-full overflow-hidden rounded-lg bg-white shadow-md transition-shadow duration-300 hover:shadow-lg"
        >
          <!-- Recipe Content -->
          <div class="flex-1 flex flex-col p-6">
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
                <span>{{ getAuthorName(recipe.author_id) }}</span>
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
            <div class="flex items-center justify-end mt-auto">
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
      <div v-if="recipes.length === 0" class="py-12 text-center">
        <div class="mb-4 text-6xl">🍳</div>
        <h3 class="mb-2 text-xl font-medium text-gray-900">No recipes found</h3>
        <p class="text-gray-600">Start by adding your first recipe!</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { mockRecipes, getUserById, type Recipe } from "../data/mockRecipes";

const recipes = ref<Recipe[]>([]);

const getAuthorName = (authorId: number): string => {
  const user = getUserById(authorId);
  return user ? user.username : "Unknown Chef";
};

onMounted(() => {
  // Simulate loading recipes
  recipes.value = mockRecipes;
});
</script>
