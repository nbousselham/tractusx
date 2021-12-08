import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import { protectedRoutes } from "./protected";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "",
    redirect: {
      name: "dashboard",
    },
  },
  ...protectedRoutes,
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
  scrollBehavior() {
    return {
      x: 0,
      y: 0,
    };
  },
});

export default router;
