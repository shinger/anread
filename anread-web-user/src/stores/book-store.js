import { defineStore } from "pinia";

export const useBookStore = defineStore('book', {
  state: () => ({
    bookInfo: {},
  }),
  actions: {
    setBookInfo(bookInfo) {
      this.bookInfo = bookInfo;
    },
  },
});
