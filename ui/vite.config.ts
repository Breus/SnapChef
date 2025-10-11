import tailwindcss from "@tailwindcss/vite";
import vue from "@vitejs/plugin-vue";
import { defineConfig } from "vite";
import checker from "vite-plugin-checker";

export default defineConfig({
    base: '',
    plugins: [
        vue(),
        tailwindcss(),
        checker({
            stylelint: {
                lintCommand: 'stylelint "**/*.{css,scss,vue}"',
                watchPath: "./src",
            },
            typescript: true,
            vueTsc: true,
        }),
    ],
    server: {
        // this ensures that the browser opens upon server start
        open: true
    },
});
