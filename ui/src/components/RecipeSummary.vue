<script setup lang="ts">
defineProps(["recipeSummary"]);
</script>

<template>
    <!-- Recipe Content -->
    <div class="flex flex-1 flex-col p-6">
        <h3 class="mb-2 text-xl font-semibold text-gray-900">
            {{ recipeSummary.title }}
        </h3>
        <p class="mb-4 line-clamp-3 text-sm text-gray-600">
            {{ recipeSummary.description }}
        </p>

        <!-- Recipe Meta -->
        <div class="mb-4 flex items-center justify-start text-sm text-gray-500">
            <div class="flex items-center">
                <span class="mr-1">By </span>
                <span>{{ recipeSummary.author.username }}</span>
            </div>
        </div>

        <!-- View Recipe Button -->
        <div class="mt-auto flex items-center justify-end">
            <button @click="$router.push(`/recipe/${recipeSummary.id}`)"
                    class="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white transition-colors duration-200 hover:bg-blue-700">
                View Recipe
            </button>
        </div>
    </div>
</template>
