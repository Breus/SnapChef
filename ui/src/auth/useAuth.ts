import { ref } from "vue";

const userId = ref<string | null>(localStorage.getItem("userId"));
const userName = ref<string | null>(localStorage.getItem("userName"));
const authToken = ref<string | null>(localStorage.getItem("authToken"));

export function useAuth() {
    function login(userIdArg: string, userNameArg: string, token: string) {
        userId.value = userIdArg;
        localStorage.setItem("userId", userIdArg);
        userName.value = userNameArg;
        localStorage.setItem("userName", userNameArg);
        authToken.value = token;
        localStorage.setItem("authToken", token);
    }
    function logout() {
        userId.value = null;
        localStorage.removeItem("userId");
        userName.value = null;
        localStorage.removeItem("username");
        authToken.value = null;
        localStorage.removeItem("authToken");
    }
    return { userId, userName, authToken, login, logout };
}
