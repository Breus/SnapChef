import { ref } from "vue";
import { Mutex } from "async-mutex";

const refreshAccessTokenMutex = new Mutex();
let refreshInProgress: Promise<void> | null = null;

const storedUserId = localStorage.getItem("userId");
const userId = ref<number | null>(storedUserId !== null ? parseInt(storedUserId) : null);
const username = ref<string | null>(localStorage.getItem("username"));
const accessToken = ref<string | null>(localStorage.getItem("accessToken"));
const refreshToken = ref<string | null>(localStorage.getItem("refreshToken"));

// Store actual expiry timestamps instead of durations
const storedAccessTokenExpiry = localStorage.getItem("accessTokenExpiry");
const accessTokenExpiry = ref<number | null>(storedAccessTokenExpiry !== null ? parseInt(storedAccessTokenExpiry) : null);
const storedRefreshTokenExpiry = localStorage.getItem("refreshTokenExpiry");
const refreshTokenExpiry = ref<number | null>(storedRefreshTokenExpiry !== null ? parseInt(storedRefreshTokenExpiry) : null);

function shouldRefreshAccessToken(): boolean {
    if (!accessTokenExpiry.value) {
        return false;
    }
    return Date.now() >= (accessTokenExpiry.value - 3000);
}

function refreshTokenIsExpired(): boolean {
    if (!refreshTokenExpiry.value) {
        return true;
    }
    return Date.now() >= (refreshTokenExpiry.value - 1000);
}

export function useAuth() {
    function login(userIdArg: number, userNameArg: string, accessTokenArg: string, accessExpiresInSeconds: number, refreshTokenArg: string,
                   refreshExpiresInSecondsArg: number) {
        // Calculate actual expiry timestamps. Minus 2 seconds to account for potential clock skew and latency.
        const accessExpiryTime = Date.now() + ((accessExpiresInSeconds - 2) * 1000);
        const refreshExpiryTime = Date.now() + ((refreshExpiresInSecondsArg - 2) * 1000);

        // Store user info
        userId.value = userIdArg;
        localStorage.setItem("userId", userIdArg.toString());
        username.value = userNameArg;
        localStorage.setItem("username", userNameArg);
        accessToken.value = accessTokenArg;
        localStorage.setItem("accessToken", accessTokenArg);
        accessTokenExpiry.value = accessExpiryTime;
        localStorage.setItem("accessTokenExpiry", accessExpiryTime.toString());

        refreshToken.value = refreshTokenArg;
        localStorage.setItem("refreshToken", refreshTokenArg);
        refreshTokenExpiry.value = refreshExpiryTime;
        localStorage.setItem("refreshTokenExpiry", refreshExpiryTime.toString());
    }

    function logout() {
        userId.value = null;
        localStorage.removeItem("userId");
        username.value = null;
        localStorage.removeItem("username");
        accessToken.value = null;
        localStorage.removeItem("accessToken");
        accessTokenExpiry.value = null;
        localStorage.removeItem("accessTokenExpiry");
        refreshToken.value = null;
        localStorage.removeItem("refreshToken");
        refreshTokenExpiry.value = null;
        localStorage.removeItem("refreshTokenExpiry");
    }

    /**
     * Updates only the access token and its expiry time
     * Used when refreshing access tokens
     */
    function updateAccessToken(newAccessToken: string, expiresInSeconds: number) {
        // Calculate the actual expiry timestamp (with a buffer of 2 seconds for clock drift and latency)
        const accessExpiryTime = Date.now() + ((expiresInSeconds - 2) * 1000);
        accessToken.value = newAccessToken;
        localStorage.setItem("accessToken", newAccessToken);
        accessTokenExpiry.value = accessExpiryTime;
        localStorage.setItem("accessTokenExpiry", accessExpiryTime.toString());
    }


    /**
     * Checks if the user is logged in by verifying that userId and accessToken are non-null
     */
    function isLoggedIn() {
        return userId.value !== null && accessToken.value !== null;
    }

    async function ensureFreshAccessToken(
        refreshAccessTokenFn: (refreshToken: string) => Promise<{
            accessToken: string;
            expiresInSeconds: number;
        }>
    ) {
        if (!shouldRefreshAccessToken()) {
            return;
        }
        if (refreshInProgress) {
            await refreshInProgress;
            return;
        }
        return refreshAccessTokenMutex.runExclusive(async () => {
            if (!shouldRefreshAccessToken()) {
                return;
            }
            if (refreshInProgress) {
                await refreshInProgress;
                return;
            }
            refreshInProgress = (async () => {
                try {
                    if (!refreshToken.value || refreshTokenIsExpired()) {
                        logout();
                        console.log("Token refresh failed: Refresh token is expired");
                        return;
                    }
                    const response = await refreshAccessTokenFn(refreshToken.value);
                    updateAccessToken(response.accessToken, response.expiresInSeconds);
                } catch (error) {
                    console.error("Token refresh failed:", error);
                    throw error;
                } finally {
                    refreshInProgress = null;
                }
            })();
            await refreshInProgress;
        });
    }

    return {
        userId,
        username,
        accessToken,
        refreshToken,
        isLoggedIn,
        login,
        logout,
        ensureFreshAccessToken
    };
}
