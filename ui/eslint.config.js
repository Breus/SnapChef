import { defineConfig } from "eslint/config";
import pluginVue from "eslint-plugin-vue";
import globals from "globals";
import tseslint from "typescript-eslint";

// ESLint configuration focused on Vue files only
// JavaScript and TypeScript linting is handled by Biome
export default defineConfig([
    // Vue specific configuration
    {
        extends: [pluginVue.configs["flat/essential"]],
        files: ["**/*.vue"],
        languageOptions: {
            globals: { ...globals.browser, ...globals.node },
            parserOptions: { parser: tseslint.parser },
        },
        plugins: { vue: pluginVue },
    },
]);
