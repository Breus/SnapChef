import { createRouter, createWebHistory } from "vue-router";
import CreateRecipe from "../components/CreateRecipe.vue";
import Login from "../components/LoginView.vue";
import RecipeSummaryList from "../components/RecipeSummaryList.vue";
import RecipeView from "../components/RecipeView.vue";
import ProfileView from "../components/ProfileView.vue";

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
        component: CreateRecipe,
        name: "EditRecipe",
        path: "/recipe/:id/edit",
    },
    {
        component: Login,
        name: "Login",
        path: "/login",
    },
    {
        component: ProfileView,
        name: "Profile",
        path: "/profile",
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
