import { ref } from "vue";


const storedUserId = localStorage.getItem("userId");
const userId = ref<number | null>(storedUserId !== null ? parseInt(storedUserId) : null);
const userName = ref<string | null>(localStorage.getItem("userName"));
const authToken = ref<string | null>(localStorage.getItem("authToken"));
const refreshToken = ref<string | null>(localStorage.getItem("refreshToken"));

/*
 * This is a hook that provides access to the authentication state.
 */
export function authLocalState() {
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
        localStorage.removeItem("userName");
        authToken.value = null;
        localStorage.removeItem("authToken");
        refreshToken.value = null;
        localStorage.removeItem("refreshToken");
    }

    return {userId, userName, authToken, login, logout};
}
