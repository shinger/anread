import { defineStore } from "pinia";

export const useInitStore = defineStore('init', {
  state: () => ({
    highlightColor: "",
  }),
  actions: {
    setHighlightColor(color) {
      this.highlightColor = color;
      localStorage.setItem("highlightColor", color);
    },
  },
});
