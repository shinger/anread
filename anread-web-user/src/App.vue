<!--
 * @Author: huajingyang 3373238891@qq.com
 * @Date: 2026-03-05 09:41:19
 * @LastEditors: huajingyang 3373238891@qq.com
 * @LastEditTime: 2026-03-05 10:04:38
 * @FilePath: \anread\anread-web-user\src\App.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<script setup>
import Header from "@/components/Header.vue";
import Mask from "@/components/Mask.vue";
import SearchMask from "@/components/SearchMask.vue";
import Message from "@/components/Message.vue";
import { onMounted } from "vue";
import { useThemeStore } from "@/stores/theme-store.js";

const themeStore = useThemeStore();

onMounted(() => {
  console.log(themeStore.themeLight);
  if (themeStore.themeLight) {
    document.documentElement.setAttribute("data-theme", "light");
  } else {
    document.documentElement.setAttribute("data-theme", "dark");
  }
});
</script>

<template>
  <div
    class="min-h-screen max-w-screen bg-gray-100 dark:bg-stone-900 dark:text-white"
  >
    <Header />
    <RouterView />
    <Mask />
    <SearchMask />
    <Message />
  </div>
</template>

<style>
</style>
