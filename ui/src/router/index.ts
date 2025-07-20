import { createRouter, createWebHistory } from "vue-router";
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
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
