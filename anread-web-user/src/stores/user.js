// src/stores/user.js
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null,
  }),
  actions: {
    setUserInfo(userInfo) {
      this.userInfo = userInfo;
    },
    getUserId() {
      return this.userInfo?.userId || null;
    },
  },
});