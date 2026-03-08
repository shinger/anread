import { defineStore } from "pinia";

export const useSearchStore = defineStore('search', {
  state: () => ({
    searchResult: [], // 搜索结果
  }),
  actions: {
    setSearchResult(data) {
      this.searchResult = data;
    },
    getSearchResult() {
      return this.searchResult;
    }
  },
});
