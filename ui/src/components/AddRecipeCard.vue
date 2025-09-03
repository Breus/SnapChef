<script setup lang="ts">
import { useRouter } from "vue-router";

const props = defineProps<{ isLoggedIn: boolean }>();
const router = useRouter();

function handleClick() {
    if (props.isLoggedIn) {
        router.push("/recipe/create")
    } else {
        router.push("/login");
    }
}
</script>

<template>
    <div @click="handleClick"
        class="flex flex-col items-center justify-center cursor-pointer rounded-lg border-2 border-dashed border-gray-300 bg-white shadow-sm hover:border-gray-400 hover:shadow-md transition-all duration-150 p-8">
        <span class="text-6xl text-gray-400 mb-2">+</span>
        <span class="text-lg font-medium text-gray-600">Add Recipe</span>
    </div>
</template>