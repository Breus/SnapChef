import { ref, computed } from "vue";


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

// Computed values to check if tokens are expired
const isAccessTokenExpired = computed(() => {
    if (!accessTokenExpiry.value) {
        return true;
    }
    return Date.now() >= accessTokenExpiry.value;
});

const isRefreshTokenExpired = computed(() => {
    if (!refreshTokenExpiry.value) return true;
    return Date.now() >= refreshTokenExpiry.value;
});

// Need to refresh token 3 seconds before it expires
const shouldRefreshToken = computed(() => {
    if (!accessTokenExpiry.value) {
        return false;
    }
    // Refresh 3 seconds before expiry
    return Date.now() >= (accessTokenExpiry.value - 3000);
});

export function useAuth() {
    function login(userIdArg: number, userNameArg: string, accessTokenArg: string, accessExpiresInSeconds: number, refreshTokenArg: string,
                   refreshExpiresInSecondsArg: number) {
        // Calculate actual expiry timestamps. Minus 3 seconds to account for potential clock skew and latency.
        const accessExpiryTime = Date.now() + ((accessExpiresInSeconds - 3) * 1000);
        const refreshExpiryTime = Date.now() + ((refreshExpiresInSecondsArg - 3) * 1000);

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
        // Calculate actual expiry timestamp (with 3 seconds buffer for clock skew)
        const accessExpiryTime = Date.now() + ((expiresInSeconds - 3) * 1000);
        accessToken.value = newAccessToken;
        localStorage.setItem("accessToken", newAccessToken);
        accessTokenExpiry.value = accessExpiryTime;
        localStorage.setItem("accessTokenExpiry", accessExpiryTime.toString());
    }

    return {
        userId,
        username,
        accessToken,
        refreshToken,
        isAccessTokenExpired,
        isRefreshTokenExpired,
        shouldRefreshToken,
        login,
        logout,
        updateAccessToken
    };
}
