import { createRouter, createWebHistory } from "vue-router";
import RecipeList from "../components/RecipeList.vue";
import RecipeView from "../components/RecipeView.vue";

const routes = [
  {
    path: "/",
    name: "RecipeList",
    component: RecipeList,
  },
  {
    path: "/recipe/:id",
    name: "RecipeView",
    component: RecipeView,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
