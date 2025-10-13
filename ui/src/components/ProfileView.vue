<script setup lang="ts">
import {useAuth} from "../auth/useAuth";
import {useRouter} from "vue-router";

const {username, userId, logout} = useAuth();
const router = useRouter();

const handleLogout = () => {
    logout();
    router.push("/");
};
</script>

<template>
    <div class="min-h-screen bg-gray-50 pt-6">
        <div class="mx-auto max-w-lg px-4">
            <div class="mb-6">
                <button @click="$router.push('/')"
                        class="cursor-pointer inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50">
                    <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                    </svg>
                    Back to Recipes
                </button>
            </div>

            <div class="overflow-hidden rounded-xl bg-white shadow">
                <div class="px-6 py-8">
                    <div class="flex flex-col items-center justify-center mb-6">
                        <div class="h-20 w-20 rounded-full bg-green-100 flex items-center justify-center mb-4">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-green-600" viewBox="0 0 24 24"
                                 fill="none" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round"
                                      d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75a7.488 7.488 0 0 0-5.982 2.975m11.963 0a9 9 0 1 0-11.963 0m11.963 0A8.966 8.966 0 0 1 12 21a8.966 8.966 0 0 1-5.982-2.275M15 9.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
                            </svg>
                        </div>
                        <h1 class="text-2xl font-bold text-gray-900">{{ username }}</h1>
                        <p class="text-sm text-gray-500">User ID: {{ userId }}</p>
                    </div>

                    <div class="border-t border-gray-200 pt-6">
                        <button
                            @click="handleLogout"
                            class="w-full flex items-center justify-center rounded-md bg-white px-4 py-3 text-sm font-medium text-red-600 shadow-sm border border-gray-200 hover:bg-gray-50"
                        >
                            Log Out
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
