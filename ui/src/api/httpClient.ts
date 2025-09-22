import type { ErrorResponseBody } from "../models/dto/ErrorResponseBody.ts";

const API_BASE_URL = "http://localhost:8080";

// Types for HTTP client
export type HttpMethod = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

export interface RequestConfig {
    headers?: Record<string, string>;
    params?: Record<string, string>;
    timeout?: number;
    signal?: AbortSignal;
}

export interface HttpClient {
    get<T>(path: string, config?: RequestConfig): Promise<T>;
    post<T>(path: string, data?: unknown, config?: RequestConfig): Promise<T>;
    put<T>(path: string, data?: unknown, config?: RequestConfig): Promise<T>;
    delete<T>(path: string, config?: RequestConfig): Promise<T>;
    patch<T>(path: string, data?: unknown, config?: RequestConfig): Promise<T>;
    request<T>(method: HttpMethod, path: string, data?: unknown, config?: RequestConfig): Promise<T>;
}

/**
 * Represents an HTTP error with status code, status text, and optional response data.
 * @template T - The type of the response data. Defaults to unknown for type safety.
 */
export class HttpError<T = unknown> extends Error {
    status: number;
    statusText: string;
    data?: T;

    /**
     * Creates a new HttpError instance.
     * @param status - The HTTP status code
     * @param statusText - The HTTP status text
     * @param message - The error message
     * @param data - Optional response data of type T
     */
    constructor(status: number, statusText: string, message: string, data?: T) {
        super(message);
        this.name = "HttpError";
        this.status = status;
        this.statusText = statusText;
        this.data = data;
    }
}

// Default request configuration
const defaultConfig: RequestConfig = {
    headers: {
        "Content-Type": "application/json",
    },
};

// Helper function to build URL with query parameters
const buildUrl = (path: string, params?: Record<string, string>): string => {
    const queryString = params ? `?${new URLSearchParams(params).toString()}` : "";
    // Ensure path doesn't start with a slash when concatenating
    const normalizedPath = path.startsWith("/") ? path.substring(1) : path;
    return `${API_BASE_URL}/${normalizedPath}${queryString}`;
};

// Implementation of the HTTP client
class FetchHttpClient implements HttpClient {
    async request<T>(method: HttpMethod, path: string, data?: unknown, config?: RequestConfig): Promise<T> {
        const mergedConfig = { ...defaultConfig, ...config };
        const { headers, params, signal } = mergedConfig;

        const url = buildUrl(path, params);

        const requestOptions: RequestInit = {
            method,
            headers: { ...defaultConfig.headers, ...headers },
            signal,
        };

        if (data && ["POST", "PUT", "PATCH"].includes(method)) {
            requestOptions.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(url, requestOptions);

            // Handle non-JSON responses
            const contentType = response.headers.get("content-type");
            // Use a union type to handle both JSON objects and plain text responses
            let responseData: object | string | ErrorResponseBody;

            if (contentType?.includes("application/json")) {
                responseData = await response.json();
            } else {
                responseData = await response.text();
            }

            if (!response.ok) {
                throw new HttpError<object | string | ErrorResponseBody>(
                    response.status,
                    response.statusText,
                    `Request failed: ${response.status} ${response.statusText}`,
                    responseData,
                );
            }

            return responseData as T;
        } catch (error) {
            if (error instanceof HttpError) {
                throw error;
            }

            // Handle network errors, timeouts, etc.
            throw new Error(`Network error: ${(error as Error).message}`);
        }
    }

    async get<T>(path: string, config?: RequestConfig): Promise<T> {
        return this.request<T>("GET", path, undefined, config);
    }

    async post<T>(path: string, data?: unknown, config?: RequestConfig): Promise<T> {
        return this.request<T>("POST", path, data, config);
    }

    async put<T>(path: string, data?: unknown, config?: RequestConfig): Promise<T> {
        return this.request<T>("PUT", path, data, config);
    }

    async delete<T>(path: string, config?: RequestConfig): Promise<T> {
        return this.request<T>("DELETE", path, undefined, config);
    }

    async patch<T>(path: string, data?: unknown, config?: RequestConfig): Promise<T> {
        return this.request<T>("PATCH", path, data, config);
    }
}

// Create and export a singleton instance of the HTTP client
export const httpClient = new FetchHttpClient();

// Export individual methods for backward compatibility and convenience
export const get = <T>(path: string, config?: RequestConfig): Promise<T> => {
    return httpClient.get<T>(path, config);
};

export const post = <T>(path: string, data?: unknown, config?: RequestConfig): Promise<T> => {
    return httpClient.post<T>(path, data, config);
};

export const put = <T>(path: string, data?: unknown, config?: RequestConfig): Promise<T> => {
    return httpClient.put<T>(path, data, config);
};

export const del = <T>(path: string, config?: RequestConfig): Promise<T> => {
    return httpClient.delete<T>(path, config);
};

export const patch = <T>(path: string, data?: unknown, config?: RequestConfig): Promise<T> => {
    return httpClient.patch<T>(path, data, config);
};
