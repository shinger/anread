// src/stores/theme-store.js
import { defineStore } from "pinia";

export const useThemeStore = defineStore("theme", {
  state: () => ({
    themeLight: localStorage.getItem("themeLight") === "true",
  }),
  actions: {
    setTheme(themeLight) {
      this.themeLight = themeLight;
      document.documentElement.setAttribute(
        "data-theme",
        themeLight ? "light" : "dark"
      );
      localStorage.setItem("themeLight", themeLight);
    },
  },
});
