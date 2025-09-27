import { ref } from "vue";


const storedUserId = localStorage.getItem("userId");
const userId = ref<number | null>(storedUserId !== null ? parseInt(storedUserId) : null);
const userName = ref<string | null>(localStorage.getItem("userName"));
const accessToken = ref<string | null>(localStorage.getItem("accessToken"));
const refreshToken = ref<string | null>(localStorage.getItem("refreshToken"));
const storedRefreshExpiresInSeconds = localStorage.getItem("refreshExpiresInSeconds");
const refreshExpiresInSeconds = ref<number | null>(storedRefreshExpiresInSeconds !== null ? parseInt(storedRefreshExpiresInSeconds) : null);

/*
 * This is a hook that provides access to the authentication state.
 */
export function authLocalState() {
    function login(userIdArg: number, userNameArg: string, accessTokenArg: string, refreshTokenArg: string, refreshExpiresInSecondsArg: number) {
        userId.value = userIdArg;
        localStorage.setItem("userId", userIdArg.toString());
        userName.value = userNameArg;
        localStorage.setItem("userName", userNameArg);
        accessToken.value = accessTokenArg;
        localStorage.setItem("accessToken", accessTokenArg);
        refreshToken.value = refreshTokenArg;
        localStorage.setItem("refreshToken", refreshTokenArg);
        refreshExpiresInSeconds.value = refreshExpiresInSecondsArg;
        localStorage.setItem("refreshExpiresInSeconds", refreshExpiresInSecondsArg.toString());
    }

    function logout() {
        userId.value = null;
        localStorage.removeItem("userId");
        userName.value = null;
        localStorage.removeItem("userName");
        accessToken.value = null;
        localStorage.removeItem("accessToken");
        refreshToken.value = null;
        localStorage.removeItem("refreshToken");
        refreshExpiresInSeconds.value = null;
        localStorage.removeItem("refreshExpiresInSeconds");
    }

    return {userId, userName, accessToken, login, logout};
}
