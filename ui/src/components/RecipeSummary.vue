<script setup lang="ts">
import { PreparationTime } from "../models/domain/PreparationTime.ts";
import type RecipeSummary from "../models/domain/RecipeSummary.ts";
import { API_BASE_URL } from "../api/httpClient.ts";

defineProps<{ recipeSummary: RecipeSummary }>();

</script>

<template>
    <RouterLink :to="`/recipe/${recipeSummary.id}`" class="block h-full">
        <!-- Recipe Content with Image -->
        <div class="flex flex-1 gap-4 p-4 h-full">
            <!-- Thumbnail Image -->
            <div v-if="recipeSummary.hasImage" class="flex-shrink-0">
                <img 
                    :src="`${API_BASE_URL}/recipes/${recipeSummary.id}/image`"
                    :alt="recipeSummary.title"
                    class="h-22 w-22 rounded-lg object-cover"
                />
            </div>

            <!-- Text Content -->
            <div class="flex flex-1 flex-col h-full">
                <h3 class="mb-1 text-md font-semibold text-gray-900">
                    {{ recipeSummary.title }}
                </h3>
                <p class="mb-4 line-clamp-1 text-sm text-gray-600">
                    {{ recipeSummary.description }}
                </p>

                <div class="flex items-center justify-between text-sm text-gray-500 w-full mt-auto">
                    <div class="flex items-center">
                        <span class="mr-1">
                                <!-- Servings icon -->
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor"
                                     class="size-4">
                                    <path
                                        d="M8 8a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5ZM3.156 11.763c.16-.629.44-1.21.813-1.72a2.5 2.5 0 0 0-2.725 1.377c-.136.287.102.58.418.58h1.449c.01-.077.025-.156.045-.237ZM12.847 11.763c.02.08.036.16.046.237h1.446c.316 0 .554-.293.417-.579a2.5 2.5 0 0 0-2.722-1.378c.374.51.653 1.09.813 1.72ZM14 7.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0ZM3.5 9a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3ZM5 13c-.552 0-1.013-.455-.876-.99a4.002 4.002 0 0 1 7.753 0c.136.535-.324.99-.877.99H5Z"/>
                                </svg>
                            </span>
                        <span>{{ recipeSummary.numServings }} servings</span>
                    </div>
                    <div v-if="recipeSummary.preparationTime" class="flex items-center">
                        <span class="mr-1">
                            <!-- Preparation time icon -->
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor"
                                 class="size-4">
                                <path fill-rule="evenodd"
                                      d="M1 8a7 7 0 1 1 14 0A7 7 0 0 1 1 8Zm7.75-4.25a.75.75 0 0 0-1.5 0V8c0 .414.336.75.75.75h3.25a.75.75 0 0 0 0-1.5h-2.5v-3.5Z"
                                      clip-rule="evenodd"/>
                            </svg>
                        </span>
                        <span>{{
                                PreparationTime[recipeSummary.preparationTime as keyof typeof PreparationTime]
                            }}
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </RouterLink>
</template>
