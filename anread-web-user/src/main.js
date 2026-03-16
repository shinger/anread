import "./assets/main.css";
import { watchSystemTheme } from "./utils/theme.js";
import { useThemeStore } from "@/stores/theme-store.js";
import { useInitStore } from "@/stores/init-store.js";
import { createApp } from "vue";
import { createPinia } from "pinia";
import "vue-toast-notification/dist/theme-default.css";
import createToast from "vue-toast-notification";

import App from "./App.vue";
import router from "./router";

const app = createApp(App);
app.config.devtools = true;

app.use(createPinia());
app.use(router);
app.use(createToast, {
  position: "top-right",
});

app.mount("#app");

// 初始化系统主题
const themeStore = useThemeStore();
watchSystemTheme((theme) => {
  document.documentElement.setAttribute("data-theme", theme);
  themeStore.setTheme(theme === "light");
});

// 初始化用户个性化设置
const initStore = useInitStore();
if (localStorage.getItem("highlightColor")) {
  initStore.setHighlightColor(localStorage.getItem("highlightColor"));
} else {
  initStore.setHighlightColor("yellow");
}
