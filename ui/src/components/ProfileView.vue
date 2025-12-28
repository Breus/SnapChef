<script setup lang="ts">
import {useAuth} from "../auth/useAuth";
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import { getUser, updateEmail, updatePassword } from "../api/userAccountApi";
import type UserAccount from "../models/domain/UserAccount";

const {username, userId, logout} = useAuth();
const router = useRouter();
const isLoading = ref<boolean>(true);

// Email state
const showEmailForm = ref(false);
const emailData = ref({
    currentEmail: '',
    newEmail: '',
    currentPassword: ''
});
const emailIsSubmitting = ref(false);
const emailSuccess = ref(false);
const emailError = ref<string | null>(null);

// Password state
const showPasswordForm = ref(false);
const passwordData = ref({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
});
const passwordIsSubmitting = ref(false);
const passwordSuccess = ref(false);
const passwordError = ref<string | null>(null);

onMounted(async () => {
    await fetchUserEmail();
});

const fetchUserEmail = async () => {
    if (userId.value !== null) {
        try {
            const userAccount: UserAccount = await getUser(userId.value);
            emailData.value.currentEmail = userAccount.email;
        } catch (err) {
            emailError.value = "Failed to load user email.";
        } finally {
            isLoading.value = false;
        }
    }
};

const handleLogout = () => {
    logout();
    router.push("/");
};

const submitEmailUpdate = async () => {
    emailSuccess.value = false;

    if (!emailData.value.newEmail || !emailData.value.currentPassword) {
        emailError.value = 'Please fill in all fields';
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(emailData.value.newEmail)) {
        emailError.value = 'Please enter a valid email address';
        return;
    }

    if (!userId || userId.value === null) {
        emailError.value = 'User ID is not available';
        return;
    }

    emailIsSubmitting.value = true;

    try {
        await updateEmail(userId.value, emailData.value.newEmail, emailData.value.currentPassword);
        emailSuccess.value = true;
        emailError.value = null;
        await fetchUserEmail();
    } catch (error: any) {
        emailError.value = error || 'Failed to update email';
    } finally {
        emailIsSubmitting.value = false;
    }
};

const submitPasswordUpdate = async () => {
    passwordSuccess.value = false;

    if (!passwordData.value.currentPassword || !passwordData.value.newPassword || !passwordData.value.confirmPassword) {
        passwordError.value = 'Please fill in all fields';
        return;
    }

    if (passwordData.value.newPassword.length < 8) {
        passwordError.value = 'New password must be at least 8 characters';
        return;
    }

    if (passwordData.value.newPassword !== passwordData.value.confirmPassword) {
        passwordError.value = 'New passwords do not match';
        return;
    }

    if (!userId || userId.value === null) {
        passwordError.value = 'User ID is not available';
        return;
    }

    passwordIsSubmitting.value = true;
    
    try {
        await updatePassword(userId.value, passwordData.value.currentPassword, passwordData.value.newPassword);
        passwordSuccess.value = true;
        passwordError.value = null;
        passwordData.value.currentPassword = '';
        passwordData.value.newPassword = '';
        passwordData.value.confirmPassword = '';
    } catch (error: any) {
        passwordError.value = error || 'Failed to update password';
    } finally {
        passwordIsSubmitting.value = false;
    }
};
</script>

<template>
    <div class="min-h-screen bg-gray-50 pt-6">
        <div v-if="!isLoading" class="mx-auto max-w-lg px-4">
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
                        <p class="text-sm text-gray-500">{{ emailData.currentEmail }}</p>
                        <p class="text-sm text-gray-500">User ID: {{ userId }}</p>
                    </div>

                    <!-- Account Settings Section -->
                    <div class="border-t border-gray-200 pt-6 space-y-4 mb-6">

                        <!-- Update Email -->
                        <div class="border border-gray-200 rounded-lg overflow-hidden">
                            <button
                                @click="showEmailForm = !showEmailForm"
                                class="w-full flex items-center justify-between px-4 py-3 bg-white hover:bg-gray-50 transition-colors"
                            >
                                <div class="flex items-center">
                                    <svg class="h-5 w-5 text-gray-400 mr-3" fill="none" stroke="currentColor"
                                         viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                                    </svg>
                                    <span class="text-sm font-medium text-gray-700">Update Email Address</span>
                                </div>
                                <svg class="h-5 w-5 text-gray-400 transition-transform"
                                     :class="{'rotate-180': showEmailForm}"
                                     fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M19 9l-7 7-7-7"/>
                                </svg>
                            </button>

                            <div v-if="showEmailForm" class="px-4 pb-4 bg-gray-50">
                                <form @submit.prevent="submitEmailUpdate" class="space-y-3 pt-3">
                                    <div>
                                        <label class="block text-xs font-medium text-gray-700 mb-1">Current password</label>
                                        <input
                                            v-model="emailData.currentPassword"
                                            type="password"
                                            placeholder="Current password"
                                            class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-shadow"
                                            :disabled="emailIsSubmitting"
                                        />
                                    </div>
                                    <div>
                                        <label class="block text-xs font-medium text-gray-700 mb-1">New e-mail address</label>
                                        <input
                                            v-model="emailData.newEmail"
                                            type="email"
                                            placeholder="your.email@example.com"
                                            class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-shadow"
                                            :disabled="emailIsSubmitting"
                                        />
                                    </div>

                                    <div v-if="emailError"
                                         class="flex items-center text-xs text-red-600 bg-red-50 px-3 py-2 rounded-md">
                                        <svg class="h-4 w-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                                                  clip-rule="evenodd"/>
                                        </svg>
                                        {{ emailError }}
                                    </div>

                                    <div v-if="emailSuccess"
                                         class="flex items-center text-xs text-green-600 bg-green-50 px-3 py-2 rounded-md">
                                        <svg class="h-4 w-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                                  clip-rule="evenodd"/>
                                        </svg>
                                        E-mail address updated successfully!
                                    </div>

                                    <div class="flex gap-2 pt-1">
                                        <button
                                            type="submit"
                                            :disabled="emailIsSubmitting"
                                            class="flex-1 px-4 py-2 bg-green-600 text-white text-sm font-medium rounded-md hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                                        >
                                            <span v-if="!emailIsSubmitting">Update Email</span>
                                            <span v-else class="flex items-center justify-center">
                                                <svg class="animate-spin h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24">
                                                    <circle class="opacity-25" cx="12" cy="12" r="10"
                                                            stroke="currentColor" stroke-width="4"></circle>
                                                    <path class="opacity-75" fill="currentColor"
                                                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                                </svg>
                                                Updating...
                                            </span>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Update Password -->
                        <div class="border border-gray-200 rounded-lg overflow-hidden">
                            <button
                                @click="showPasswordForm = !showPasswordForm"
                                class="w-full flex items-center justify-between px-4 py-3 bg-white hover:bg-gray-50 transition-colors"
                            >
                                <div class="flex items-center">
                                    <svg class="h-5 w-5 text-gray-400 mr-3" fill="none" stroke="currentColor"
                                         viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                                    </svg>
                                    <span class="text-sm font-medium text-gray-700">Update Password</span>
                                </div>
                                <svg class="h-5 w-5 text-gray-400 transition-transform"
                                     :class="{'rotate-180': showPasswordForm}"
                                     fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M19 9l-7 7-7-7"/>
                                </svg>
                            </button>

                            <div v-if="showPasswordForm" class="px-4 pb-4 bg-gray-50">
                                <form @submit.prevent="submitPasswordUpdate" class="space-y-3 pt-3">
                                    <div>
                                        <label class="block text-xs font-medium text-gray-700 mb-1">Current password</label>
                                        <input
                                            v-model="passwordData.currentPassword"
                                            type="password"
                                            placeholder="Enter your current password"
                                            class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-shadow"
                                            :disabled="passwordIsSubmitting"
                                        />
                                    </div>
                                    <div>
                                        <label class="block text-xs font-medium text-gray-700 mb-1">New password</label>
                                        <input
                                            v-model="passwordData.newPassword"
                                            type="password"
                                            placeholder="Enter new password (min. 8 characters)"
                                            class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-shadow"
                                            :disabled="passwordIsSubmitting"
                                        />
                                    </div>
                                    <div>
                                        <label class="block text-xs font-medium text-gray-700 mb-1">Confirm new password</label>
                                        <input
                                            v-model="passwordData.confirmPassword"
                                            type="password"
                                            placeholder="Confirm your new password"
                                            class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none transition-shadow"
                                            :disabled="passwordIsSubmitting"
                                        />
                                    </div>

                                    <div v-if="passwordError"
                                         class="flex items-center text-xs text-red-600 bg-red-50 px-3 py-2 rounded-md">
                                        <svg class="h-4 w-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                                                  clip-rule="evenodd"/>
                                        </svg>
                                        {{ passwordError }}
                                    </div>

                                    <div v-if="passwordSuccess"
                                         class="flex items-center text-xs text-green-600 bg-green-50 px-3 py-2 rounded-md">
                                        <svg class="h-4 w-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                                                  clip-rule="evenodd"/>
                                        </svg>
                                        Password updated successfully!
                                    </div>

                                    <div class="flex gap-2 pt-1">
                                        <button
                                            type="submit"
                                            :disabled="passwordIsSubmitting"
                                            class="flex-1 px-4 py-2 bg-green-600 text-white text-sm font-medium rounded-md hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                                        >
                                            <span v-if="!passwordIsSubmitting">Update password</span>
                                            <span v-else class="flex items-center justify-center">
                                                <svg class="animate-spin h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24">
                                                    <circle class="opacity-25" cx="12" cy="12" r="10"
                                                            stroke="currentColor" stroke-width="4"></circle>
                                                    <path class="opacity-75" fill="currentColor"
                                                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                                </svg>
                                                Updating...
                                            </span>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
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
