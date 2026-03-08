import { ref } from "vue";
import { defineStore } from "pinia";

export const useLoginStore = defineStore("loginStore", () => {
  const isLogin = ref(false);

  const updateLogin = () => {
    isLogin.value = localStorage.getItem("token") !== null;
  };

  return {
    isLogin,
    updateLogin,
  };
});