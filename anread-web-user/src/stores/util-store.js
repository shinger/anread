import { defineStore } from "pinia";

export const useUtilStore = defineStore("util", {
  state: () => ({
    mask: false, // 全局遮罩层
    specifyMask: "",
    message: "", // 全局消息提示
  }),
  actions: {
    showMask(specify) { // 打开遮罩层，specify可选参数，指定遮罩层类型
      this.mask = true;
      if (specify) {
        this.specifyMask = specify;
      } else {
        this.specifyMask = "none";
      }
    },
    hideMask() {
      this.mask = false;
    },
    popup(value) { // 打开弹窗
        console.log(value)
        value = !value;
        this.mask = value;
    },
    showMessage(message) {
      this.message = message;
      setTimeout(() => {
        this.message = "";
      }, 1000);
    },
  },
});
