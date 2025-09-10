import { ref } from "vue";

const userId = ref<number | null>(localStorage.getItem("userId") ? parseInt(localStorage.getItem("userId")!) : null);
const userName = ref<string | null>(localStorage.getItem("userName"));
const authToken = ref<string | null>(localStorage.getItem("authToken"));

export function useAuth() {
    function login(userIdArg: number, userNameArg: string, token: string) {
        userId.value = userIdArg;
        localStorage.setItem("userId", userIdArg.toString());
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
