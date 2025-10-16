<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import {HttpError} from "../api/httpClient.ts";
import type {ErrorResponseBody} from "../models/dto/ErrorResponseBody.ts";
import {submitLogin, submitRegistration} from "../api/userAuthenticationApi.ts";
import {useAuth} from "../auth/useAuth.ts";

const {login} = useAuth();

const router = useRouter();

function isErrorResponse(obj: unknown): obj is ErrorResponseBody {
    if (typeof obj !== "object" || obj === null) return false;
    const o = obj as Record<string, unknown>;
    return typeof o.title === "string" && typeof o.detail === "string" && typeof o.status === "number";
}

const username = ref("");
const emailAddress = ref("");
const password = ref("");
const confirmPassword = ref("");

const error = ref<string | null>(null);
const isSubmitting = ref(false);

const submitForm = async () => {
    isSubmitting.value = true;
    try {
        if (!username.value.trim()) {
            throw new Error("The username field is required.");
        }
        if (username.value.length < 3) {
            throw new Error("The username must contain at least 3 characters.");
        }
        if (!emailAddress.value.trim()) {
            throw new Error("The email address field is required.");
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailAddress.value.trim())) {
            throw new Error("The email address you entered is not valid.");
        }
        if (password.value !== confirmPassword.value) {
            throw new Error("The passwords you entered do not match.");
        }
        if (!password.value.trim()) {
            throw new Error("The password field is required.");
        }
        if (password.value.length < 8) {
            throw new Error("The password must contain at least 8 characters.");
        }
        if (!/[A-Z]/.test(password.value)) {
            throw new Error("The password must contain at least one uppercase letter.");
        }
        if (!/[a-z]/.test(password.value)) {
            throw new Error("The password must contain at least one lowercase letter.");
        }

        const registerData = {
            username: username.value.trim(),
            emailAddress: emailAddress.value.trim(),
            password: password.value.trim(),
        };
        await submitRegistration(registerData);
        let authDetails = await submitLogin(registerData.emailAddress, registerData.password);
        login(authDetails.userId, authDetails.username, authDetails.accessToken, authDetails.expiresInSeconds, authDetails.refreshToken, authDetails.refreshExpiresInSeconds);
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
}
</script>
<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="mx-auto max-w-2xl px-4 sm:px-6 lg:px-8">
            <div class="mb-6">
                <button @click="$router.push('/')"
                        class="cursor-pointer inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm ring-1 ring-gray-300 hover:bg-gray-50">
                    <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                    </svg>
                    Back to Recipes
                </button>
            </div>
            <div class="sm:mb-4 mb-6 text-center">
                <h1 class="text-3xl md:text-4xl font-bold text-green-700">SnapChef</h1>
                <h2 class="text-md md:text-lg mt-2 text-gray-600">Add your recipes, save your favorites, and build your personal
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
                            <label for="username"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Username</label>
                            <input v-model="username" type="text" id="username"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="Used as recipe author name" required/>
                        </div>
                        <div class="mb-4">
                            <label for="emailaddress"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">E-mail</label>
                            <input v-model="emailAddress" type="email" id="emailaddress"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="username@example.com" required/>
                        </div>
                        <div class="mb-4">
                            <label for="password"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
                            <input v-model="password" id="password" type="password"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="********" required/>
                            <p class="mt-1 text-xs text-gray-500">
                                Password must be at least 8 characters and include an uppercase letter and a lowercase
                                letter.
                            </p>
                        </div>
                        <div class="mb-4">
                            <label for="confirmPassword"
                                   class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Confirm
                                Password</label>
                            <input v-model="confirmPassword" id="confirmPassword" type="password"
                                   class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-green-500 focus:border-green-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-green-500 dark:focus:border-green-500"
                                   placeholder="********" required/>
                        </div>
                    </div>
                </div>
                <div class="bg-gray-50 px-6 py-4">
                    <!-- For larger screens (sm and up) display in same row -->
                    <div class="hidden sm:flex sm:justify-between sm:items-center">
                        <div>
                            <span class="text-sm text-gray-600">Already have an account?</span>
                            <button type="button" @click="$router.push('/login')"
                                    class="ml-2 text-sm font-medium text-green-600 hover:text-green-700">
                                Login here
                            </button>
                        </div>
                        <div class="flex space-x-3">
                            <button type="button" @click="$router.push('/')"
                                    class="cursor-pointer rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2">
                                Cancel
                            </button>
                            <button type="submit"
                                    class="cursor-pointer rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                                    :disabled="isSubmitting">
                                {{ isSubmitting ? 'Registering...' : 'Register' }}
                            </button>
                        </div>
                    </div>

                    <!-- For mobile screens, stack vertically -->
                    <div class="flex flex-col space-y-4 sm:hidden">
                        <div class="flex justify-end space-x-3">
                            <button type="button" @click="$router.push('/')"
                                    class="cursor-pointer rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2">
                                Cancel
                            </button>
                            <button type="submit"
                                    class="cursor-pointer rounded-md bg-green-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                                    :disabled="isSubmitting">
                                {{ isSubmitting ? 'Registering...' : 'Register' }}
                            </button>
                        </div>
                        <div class="text-center mt-2">
                            <span class="text-sm text-gray-600">Already have an account?</span>
                            <button type="button" @click="$router.push('/login')"
                                    class="ml-2 text-sm font-medium text-green-600 hover:text-green-700">
                                Login here
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
