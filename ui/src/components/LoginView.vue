<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { authLocalState } from "../auth/authLocalState.ts";
import { HttpError } from "../api/httpClient.ts";
import { submitLogin } from "../api/userApi.ts";
import type { ErrorResponseBody } from "../models/dto/ErrorResponseBody.ts";
import type LoginCredentials from "../auth/LoginCredentials.ts";

const router = useRouter();
const {login} = authLocalState();

function isErrorResponse(obj: unknown): obj is ErrorResponseBody {
    if (typeof obj !== "object" || obj === null) return false;
    const o = obj as Record<string, unknown>;
    return typeof o.title === "string" && typeof o.detail === "string" && typeof o.status === "number";
}

// Form data
const emailaddress = ref("");
const password = ref("");

// Form state
const error = ref<string | null>(null);
const isSubmitting = ref(false);

const submitForm = async () => {
    try {
        isSubmitting.value = true;

        if (!emailaddress.value.trim()) {
            throw new Error("E-mail address is required");
        }
        if (!password.value.trim()) {
            throw new Error("Password is required");
        }
        if (password.value.length < 8) {
            throw new Error("Any valid password contains at least 8 characters");
        }
        const loginCredentials: LoginCredentials = {
            emailAddress: emailaddress.value.trim(),
            password: password.value.trim(),
        };

        let authDetails = await submitLogin(loginCredentials);
        login(authDetails.userId, authDetails.username, authDetails.accessToken, authDetails.refreshToken, authDetails.refreshExpiresInSeconds);
        // Navigate back to the homepage after successful login
        router.push("/");
    } catch (err) {
        if (err instanceof HttpError && isErrorResponse(err.data)) {
            error.value = err.data.detail ? `${err.data.title} ${err.data.detail}` : err.data.title;
        } else if (err instanceof Error) {
            error.value = err.message;
        } else {
            error.value = "An unexpected error occurred.";
        }
        console.warn("Error while logging in:", err);
    } finally {
        isSubmitting.value = false;
    }
};
</script>
<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
            <!-- Back Button -->
            <div class="mb-6">
                <button @click="$router.push('/')"
                        class="cursor-pointer inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50">
                    <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                    </svg>
                    Back to Recipes
                </button>
            </div>

            <!-- Form Header -->
            <div class="mb-8 text-center">
                <h1 class="text-3xl font-bold text-gray-900">Login Your Account</h1>
                <h2 class="text-1xl text-gray-400">Add your own recipes, save your favorites, and build your personal
                    recipe book!</h2>
            </div>
            <div v-if="error" class="mb-6 rounded-md bg-red-50 p-4">
                <div class="flex">
                    <div class="flex-shrink-0">
                        <svg class="h-5 w-5 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                        </svg>
                    </div>
                    <div class="ml-3">
                        <h3 class="text-sm font-medium text-red-800">{{ error }}</h3>
                    </div>
                </div>
            </div>

            <form @submit.prevent="submitForm" class="overflow-hidden rounded-lg bg-white shadow-lg">
                <div class="p-6">
                    <div class="mb-8">
                        <div class="mb-4">
                            <label for="emailaddress"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">E-mail</label>
                            <input v-model="emailaddress" type="email" id="emailaddress"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="username@example.com" required/>
                        </div>
                        <div class="mb-4">
                            <label for="password"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
                            <input v-model="password" id="password" type="password"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="********" required/>
                        </div>
                    </div>
                </div>
                <!-- Form Actions -->
                <div class="bg-gray-50 px-6 py-4">
                    <div class="flex justify-end space-x-3">
                        <button type="button" @click="$router.push('/')"
                                class="cursor-pointer rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2">
                            Cancel
                        </button>
                        <button type="submit"
                                class="cursor-pointer rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                                :disabled="isSubmitting">
                            {{ isSubmitting ? 'Logging in...' : 'Login' }}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
