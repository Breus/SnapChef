import { ref } from "vue";

const user = ref<string | null>(localStorage.getItem("username"));
const authToken = ref<string | null>(localStorage.getItem("authToken"));

export function useAuth() {
    function login(username: string, token: string) {
        user.value = username;
        localStorage.setItem("username", username);
        authToken.value = token;
        localStorage.setItem("authToken", token);
    }
    function logout() {
        user.value = null;
        localStorage.removeItem("username");
        authToken.value = null;
        localStorage.removeItem("authToken");
    }
    return { user, authToken, login, logout };
}
