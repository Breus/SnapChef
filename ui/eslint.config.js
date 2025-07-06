import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import pluginVue from "eslint-plugin-vue";
import json from "@eslint/json";
import css from "@eslint/css";
import { defineConfig } from "eslint/config";


export default defineConfig([
    {files: ["**/*.{js,mjs,cjs,ts,mts,cts,vue}"], plugins: {js}, extends: ["js/recommended"]},
    {files: ["**/*.{js,mjs,cjs,ts,mts,cts,vue}"], languageOptions: {globals: {...globals.browser, ...globals.node}}},
    tseslint.configs.recommended,
    pluginVue.configs["flat/essential"],
    {files: ["**/*.vue"], languageOptions: {parserOptions: {parser: tseslint.parser}}},
    {files: ["**/*.json"], plugins: {json}, language: "json/json", extends: ["json/recommended"]},
    {files: ["**/*.css"], plugins: {css}, language: "css/css", extends: ["css/recommended"]},
]);
