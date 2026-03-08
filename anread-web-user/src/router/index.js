import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import { useToast } from "vue-toast-notification";

const toast = useToast();

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
      meta: { requireAuth: false },
    },
    {
      path: "/bookshelf",
      name: "bookshelf",
      component: () => import("@/views/BookshelfView.vue"),
      meta: { requireAuth: false },
    },
    {
      path: "/book/:id",
      name: "book",
      component: () => import("@/views/BookViewV2.vue"),
      props: true,
      meta: { requireAuth: false },
    },
    {
      path: "/category",
      name: "category",
      component: () => import("@/views/CategoryView.vue"),
      meta: { requireAuth: false },
    },
    {
      path: "/introduction/:id",
      name: "introduction",
      component: () => import("@/views/IntroductionView.vue"),
      props: true,
      meta: { requireAuth: false },
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/LoginView.vue"),
      meta: { requireAuth: false },
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("@/views/ProfileView.vue"),
      meta: { requireAuth: true },
    },
    {
      path: "/test",
      name: "test",
      component: () => import("@/views/TestView.vue"),
      meta: { requireAuth: false },
    },
    {
      path: "/comment/:id",
      name: "comment",
      component: () => import("@/views/CommentView.vue"),
      props: true,
      meta: { requireAuth: false },
    }
  ],
});

router.beforeEach((to, from, next) => {
  // console.log(to);
  // console.log(window.localStorage);

  // to and from are both route objects. must call `next`.
  if (to.matched.some((m) => m.meta.requireAuth)) {
    // 需要登录
    if (window.localStorage.token) {
      next();
    } else if (to.path !== "/login") {
      let token = window.localStorage.token;
      if (token === "null" || token === "" || token === undefined) {
        next({ path: "/login" });
        toast.error("请先登录");
      }
    } else {
      next();
    }
  } else {
    // 不需要登录
    next();
  }
});

export default router;
