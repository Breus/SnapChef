import path from "node:path";
import tailwindcss from "@tailwindcss/vite";
import vue from "@vitejs/plugin-vue";
import { defineConfig } from "vite";
import checker from "vite-plugin-checker";

// https://vite.dev/config/
export default defineConfig({
    // Load environment variables from parent directory
    envDir: path.resolve(__dirname, ".."),
    plugins: [
        vue(),
        tailwindcss(),
        checker({
            stylelint: {
                lintCommand: 'stylelint "./src/**/*.{css,scss,vue}"',
                watchPath: "./src",
            },
            typescript: true,
            vueTsc: true,
        }),
    ],
});
