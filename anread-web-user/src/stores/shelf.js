import { defineStore } from "pinia";

export const useShelfStore = defineStore('shelf', {
  state: () => ({
    shelfBooks: [],
  }),
  actions: {
    setShelfBooks(shelfBooks) {
      this.shelfBooks = shelfBooks;
    },
  },
});
