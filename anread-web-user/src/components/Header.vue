<template>
  <header class="h-16 border-b border-gray-200 flex dark:text-white">
    <div class="w-1/4 h-full items-center justify-center sm:flex hidden">
      <a href="/">安阅书屋</a>
    </div>
    <div class="flex items-center justify-center ml-2">
      <Avatar
        class="w-1/8 h-full sm:hidden"
        @click="avatarIsPop = !avatarIsPop"
        :src="avatarUrl"
      />
    </div>
    <div class="sm:w-1/2 w-full h-full flex items-center justify-center z-10">
      <div class="h-3/5 w-4/5 bg-gray-200 dark:bg-stone-800 rounded-4xl">
        <input
          class="w-full h-full rounded-4xl px-4"
          type="text"
          placeholder="搜索"
          @keyup.enter="handleSearch"
          @focus="utilStore.showMask('search')"
          @blur="utilStore.hideMask()"
          v-model="searchText"
        />
      </div>
    </div>
    <div class="w-1/8 h-full flex justify-center sm:hidden">
      <MenuIcon :size="28" @click="phoneMenuUp = !phoneMenuUp" />
    </div>
    <nav
      class="absolute top-16 w-full z-20 bg-white sm:relative sm:bg-transparent sm:top-0 sm:w-1/4 sm:flex justify-center"
      :class="{ 'sm:flex': !phoneMenuUp, hidden: !phoneMenuUp }"
    >
      <ul
        class="w-full h-full flex-col sm:flex sm:flex-row items-center justify-center"
      >
        <li class="h-8 mx-2 lg:mx-4"><RouterLink to="/">首页</RouterLink></li>
        <span class="w-full block h-px sm:h-4 sm:w-0.5 bg-gray-200"></span>
        <li class="h-8 mx-2 lg:mx-4"><RouterLink to="/category">分类</RouterLink></li>
        <span class="w-full block h-px sm:h-4 sm:w-0.5 bg-gray-200"></span>
        <li class="h-8 mx-2 lg:mx-4">
          <RouterLink to="/bookshelf">书架</RouterLink>
        </li>
        <span class="w-full block h-px sm:h-4 sm:w-0.5 bg-gray-200"></span>
        <li class="h-8 mx-2 lg:mx-4" v-if="!loginStore.isLogin">
          <RouterLink to="/login">登录</RouterLink>
        </li>
        <li class="hidden sm:block mx-2 lg:mx-4" v-if="loginStore.isLogin">
          <Avatar
            @click="
              avatarIsPop = !avatarIsPop;
              utilStore.showMask();
            "
            :src="avatarUrl"
            :size="32"
          />
        </li>
      </ul>
    </nav>
    <div
      class="fixed top-14 left-0 sm:left-auto sm:right-14 rounded-md border border-gray-200 bg-white dark:bg-black z-10"
      v-show="avatarIsPop && utilStore.mask"
    >
      <ul class="w-20 text-sm flex flex-col items-center">
        <li class="my-1"><a href="/profile">个人信息</a></li>
        <li class="my-1"><a @click="logout">退出登录</a></li>
      </ul>
    </div>
  </header>
</template>
<script setup>
import Avatar from "@/components/Avatar.vue";
import MenuIcon from "@/components/icons/MenuIcon.vue";
import { useLoginStore } from "@/stores/login";
import { RouterLink } from "vue-router";
import { ref, onBeforeMount, watch } from "vue";
import request from "@/utils/request.js";
import { searchBooks } from "@/api/book-api.js";
import { useUserStore } from "@/stores/user";
import { useUtilStore } from "@/stores/util-store";
import { useSearchStore } from "@/stores/search-store";

const searchStore = useSearchStore();
const utilStore = useUtilStore();
const loginStore = useLoginStore();
const userStore = useUserStore();
const avatarIsPop = ref(false);
const avatarUrl = ref("");
const phoneMenuUp = ref(false);
const searchText = ref('');

const logout = () => {
  localStorage.removeItem("token");
  loginStore.updateLogin();
  location.reload();
};

onBeforeMount(() => {
  loginStore.updateLogin();
  fetchUserInfo();
});

// 搜索功能
const handleSearch = (event) => {
  const keyword = searchText.value.trim();
  searchBooks(keyword).then((result) => {
    console.log(result);
    searchStore.setSearchResult(result);
  });
}

// 获取用户信息
const fetchUserInfo = () => {
  if (userStore.userInfo != null) {
    userInfo.value = userStore.userInfo;
    return;
  }
  request.getUserInfo().then((result) => {
    console.log(result);
    avatarUrl.value = result.avatar;
    userStore.setUserInfo(result);
  });
};

// 监听登录状态变化
watch(() => loginStore.isLogin, (newValue, oldValue) => {
  if (!oldValue && newValue) {
    fetchUserInfo();
  }
});
</script>

<style></style>
