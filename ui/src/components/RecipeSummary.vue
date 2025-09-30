<script setup lang="ts">
import { authLocalState } from "../auth/authLocalState.ts";

defineProps(["recipeSummary"]);
const { userId } = authLocalState();
</script>

<template>
    <RouterLink :to="`/recipe/${recipeSummary.id}`" class="block h-full">
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
                    <span v-if="recipeSummary.author.userId == userId"> {{ recipeSummary.author.username }} (Me)</span>
                    <span v-else>{{ recipeSummary.author.username }}</span>
                </div>
            </div>
        </div>
    </RouterLink>
</template>
