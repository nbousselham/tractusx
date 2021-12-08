import { RouteConfig } from "vue-router";
import Dashboard from "@/views/dashboard/Dashboard.vue";
import AbstractLayout from "@/layout/AbstractLayout.vue";

export const protectedRoutes: RouteConfig[] = [
  {
    path: "/app",
    meta: {
      requiresAuth: false,
    },
    components: {
      default: AbstractLayout,
    },
    children: [
      {
        path: "dashboard",
        name: "dashboard",
        component: Dashboard,
        meta: {
          requiresAuth: false,
        },
      },
    ],
  },
];
