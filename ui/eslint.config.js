import {defineConfig} from "eslint/config";
import pluginVue from "eslint-plugin-vue";
import globals from "globals";
import tseslint from "typescript-eslint";

export default defineConfig([
    {
        extends: [pluginVue.configs["flat/essential"]],
        files: ["**/*.vue", "**/*.ts", "**/*.js"],
        languageOptions: {
            globals: {...globals.browser, ...globals.node},
            parserOptions: {parser: tseslint.parser},
        },
        plugins: {vue: pluginVue},
    },
]);
