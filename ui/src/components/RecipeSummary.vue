<script setup lang="ts">
import { useAuth } from "../auth/useAuth.ts";
import { PreparationTime } from "../models/domain/PreparationTime.ts";
import type RecipeSummary from "../models/domain/RecipeSummary.ts";

defineProps<{ recipeSummary: RecipeSummary }>();

const { userId } = useAuth();
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
            <div class="mb-4 flex flex-col items-start text-sm text-gray-500">
                <div class="flex items-center">
                    <span class="mr-1">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="size-4">
                            <path fill-rule="evenodd" d="M11.013 2.513a1.75 1.75 0 0 1 2.475 2.474L6.226 12.25a2.751 2.751 0 0 1-.892.596l-2.047.848a.75.75 0 0 1-.98-.98l.848-2.047a2.75 2.75 0 0 1 .596-.892l7.262-7.261Z" clip-rule="evenodd" />
                        </svg>
                    </span>
                    <span class="mr-1" v-if="!recipeSummary.author">Anonymous</span>
                    <span class="mr-1" v-else-if="recipeSummary.author.userId == userId"> {{ recipeSummary.author.username }} (Me)</span>
                    <span class="mr-1" v-else>{{ recipeSummary.author.username }}</span>
                </div>
                <div class="flex items-center">
                    <span class="mr-1">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="size-4">
                            <path d="M8 8a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5ZM3.156 11.763c.16-.629.44-1.21.813-1.72a2.5 2.5 0 0 0-2.725 1.377c-.136.287.102.58.418.58h1.449c.01-.077.025-.156.045-.237ZM12.847 11.763c.02.08.036.16.046.237h1.446c.316 0 .554-.293.417-.579a2.5 2.5 0 0 0-2.722-1.378c.374.51.653 1.09.813 1.72ZM14 7.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0ZM3.5 9a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3ZM5 13c-.552 0-1.013-.455-.876-.99a4.002 4.002 0 0 1 7.753 0c.136.535-.324.99-.877.99H5Z" />
                        </svg>
                    </span>
                    <span class="mr-1">{{ recipeSummary.numServings }} servings</span>
                </div>
                <div v-if="recipeSummary.preparationTime" class="flex items-center">
                    <span class="mr-1">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="size-4">
                            <path fill-rule="evenodd" d="M1 8a7 7 0 1 1 14 0A7 7 0 0 1 1 8Zm7.75-4.25a.75.75 0 0 0-1.5 0V8c0 .414.336.75.75.75h3.25a.75.75 0 0 0 0-1.5h-2.5v-3.5Z" clip-rule="evenodd" />
                        </svg>
                    </span>
                    <span class="mr-1">{{ PreparationTime[recipeSummary.preparationTime as keyof typeof PreparationTime] }}</span>
                </div>
            </div>
        </div>
    </RouterLink>
</template>
