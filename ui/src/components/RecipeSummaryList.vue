<script setup lang="ts">
import { onMounted, ref } from "vue";
import { getAllRecipeSummaries } from "../api/recipeSummaryApi.ts";
import type RecipeSummary from "../models/domain/RecipeSummary.ts";
import RecipeSummaryComponent from "./RecipeSummary.vue";
import AddRecipeCard from "./AddRecipeCard.vue";
import { useAuth } from "../auth/useAuth.ts";

const recipeSummaries = ref<RecipeSummary[]>([]);
const searchQuery = ref<string>("");
const { userId, userName, authToken, logout } = useAuth();
const isLoading = ref<boolean>(true);
const showLoading = ref<boolean>(false);
let loadingTimer: number | null = null;


const error = ref<string | null>(null);

const fetchRecipeSummaries = async () => {
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
        recipeSummaries.value = await getAllRecipeSummaries();
        error.value = null;
    } catch (err) {
        console.error("Failed to load recipe summaries:", err);
        error.value = "Failed to load recipe summaries. Please try again later.";
        recipeSummaries.value = [];
    } finally {
        isLoading.value = false;
        showLoading.value = false;
        if (loadingTimer) {
            clearTimeout(loadingTimer);
        }
    }
};

onMounted(() => {
    fetchRecipeSummaries();
});
</script>

<template>
    <div class="min-h-screen bg-gray-50">
        <div class="sticky top-0 z-20 w-full bg-green-700 shadow-lg">
            <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 flex items-center justify-between py-4">
                <span class="text-2xl font-bold text-white tracking-tight">Recipe Book</span>
                <div>
                    <template v-if="userId && userName && authToken">
                        <span class="mr-4 text-green-100">Logged in as {{ userName }}</span>
                        <button @click="logout"
                            class="cursor-pointer rounded-md bg-white px-4 py-2 text-sm font-medium text-green-700 shadow hover:bg-green-50">
                            Log Out
                        </button>
                    </template>
                    <template v-else>
                        <button @click="$router.push('/login')"
                            class="cursor-pointer rounded-md bg-white px-4 py-2 text-sm font-medium text-green-700 shadow hover:bg-green-50">
                            Log In
                        </button>
                    </template>
                </div>
            </div>
        </div>
        <div class="mx-auto mt-5 max-w-7xl px-4 sm:px-6 lg:px-8">
            <!-- Search Bar -->
            <!--            <div class="mx-auto mb-8 max-w-md">-->
            <!--                <div class="relative">-->
            <!--                    <div class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">-->
            <!--                        <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">-->
            <!--                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"-->
            <!--                                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>-->
            <!--                        </svg>-->
            <!--                    </div>-->
            <!--                    <input v-model="searchQuery" type="text" placeholder="Search recipes, ingredients, or authors..."-->
            <!--                           class="block w-full rounded-md border border-gray-300 bg-white py-2 pr-3 pl-10 leading-5 placeholder-gray-500 focus:border-blue-500 focus:placeholder-gray-400 focus:ring-1 focus:ring-blue-500 focus:outline-none"/>-->
            <!--                </div>-->
            <!--            </div>-->

            <!-- Loading State -->
            <div v-if="showLoading" class="py-12 text-center">
                <div class="mb-4 text-6xl">⏳</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">
                    Loading recipes...
                </h3>
                <p class="text-gray-600">
                    Please wait while we fetch the recipes.
                </p>
            </div>

            <!-- Error State -->
            <div v-else-if="error" class="py-12 text-center">
                <div class="mb-4 text-6xl">❌</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">Error</h3>
                <p class="text-gray-600">{{ error }}</p>
                <button @click="fetchRecipeSummaries"
                    class="mt-4 rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white transition-colors duration-200 hover:bg-blue-700">
                    Try Again
                </button>
            </div>

            <!-- Recipe Grid -->
            <div v-else-if="!showLoading && recipeSummaries.length > 0" class="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
                <AddRecipeCard :isLoggedIn="!!userName" />
                <div v-for="recipeSummary in recipeSummaries" :key="recipeSummary.id"
                    class="flex h-full flex-col overflow-hidden rounded-lg bg-white shadow-md transition-shadow duration-300 hover:shadow-lg">
                    <RecipeSummaryComponent :recipeSummary="recipeSummary" />
                </div>
            </div>

            <!-- Empty State -->
            <div v-else-if="!isLoading" class="py-12 text-center">
                <div class="mb-4 text-6xl">🍳</div>
                <h3 class="mb-2 text-xl font-medium text-gray-900">
                    No recipes found
                </h3>
                <p class="text-gray-600">Start by adding your first recipe!</p>
            </div>
        </div>
    </div>
</template>
