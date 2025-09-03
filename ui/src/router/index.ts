import { createRouter, createWebHistory } from "vue-router";
import CreateRecipe from "../components/CreateRecipe.vue";
import Login from "../components/LoginView.vue";
import RecipeSummaryList from "../components/RecipeSummaryList.vue";
import RecipeView from "../components/RecipeView.vue";

const routes = [
    {
        component: RecipeSummaryList,
        name: "RecipeList",
        path: "/",
    },
    {
        component: RecipeView,
        name: "RecipeView",
        path: "/recipe/:id",
        props: true,
    },
    {
        component: CreateRecipe,
        name: "CreateRecipe",
        path: "/recipe/create",
    },
    {
        component: Login,
        name: "Login",
        path: "/login",
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
