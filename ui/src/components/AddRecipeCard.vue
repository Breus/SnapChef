<script setup lang="ts">
import { useRouter } from "vue-router";

const props = defineProps<{ isLoggedIn: boolean }>();
const router = useRouter();

function handleClick() {
    if (props.isLoggedIn) {
        router.push("/recipe/create");
    } else {
        router.push("/login");
    }
}
</script>

<template>
    <div @click="handleClick"
         class="flex flex-row md:flex-col items-center justify-center cursor-pointer rounded-xl border-2 border-dashed border-gray-300 bg-white shadow-md hover:border-gray-400 hover:shadow-lg transition-all duration-150 p-2">
        <span class="text-4xl md:text-5xl  text-gray-400 mr-2 md:mr-0 flex items-center pb-1">+</span>
        <span class="text-md font-lg font-semibold text-gray-600 flex items-center leading-none">New Recipe</span>
    </div>
</template>
