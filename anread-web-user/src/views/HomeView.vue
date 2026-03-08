<template>
  <main class="justify-center">
    <!-- <section class="flex justify-center">
      <ul class="grid grid-cols-3 grid-rows-2 gap-8 justify-center mt-6">
        <li class="text-center"><a href="/category">分类</a></li>
        <li class="text-center"><a href="/rank">榜单</a></li>
        <li class="text-center"><a href="/vip">会员专享</a></li>
        <li class="text-center"><a href="/booklist">书单</a></li>
        <li class="text-center"><a href="/free">免费书</a></li>
        <li class="text-center"><a href="/new">11月新书</a></li>
      </ul>
    </section> -->
    <section class="flex flex-col justify-center mx-4 sm:mx-20 mt-20">
      <div class="flex flex-row justify-between mb-4">
        <div>
          <h1 class="font-bold text-xl">继续阅读</h1>
        </div>
      </div>
      <div
        class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4 justify-center"
      >
        <ul
          class="flex justify-center"
          v-for="book in shelfBooks"
          :key="book.id"
        >
          <li
            class="flex items-center rounded-xl bg-white dark:bg-black w-[80%] sm:w-200 h-32"
          >
            <a
              :href="`/book/${book.id}`"
              class="w-full h-full flex items-center"
            >
              <div class="flex items-center">
                <img
                  :src="book.cover"
                  alt=""
                  class="w-16 h-24 ml-4 object-fit"
                />
                <div class="flex flex-col justify-center ml-4">
                  <p class="text-md">{{ book.title }}</p>
                  <p class="text-xs text-gray-500 mt-2">{{ book.author }}</p>
                </div>
              </div>
            </a>
          </li>
        </ul>
      </div>
    </section>
    <section class="flex flex-col justify-center sm:mx-20">
      <div class="mt-24 flex flex-row items-center mx-8 sm:mx-0">
        <h3>大家都在看</h3>
        <a class="text-gray-500 text-sm ml-2" @click="changeBatch">换一批</a>
      </div>
      <ul
        class="grid gap-6 justify-items-center mt-12 grid-cols-1 grid-rows-4 sm:grid-cols-2 sm:grid-rows-2 lg:grid-cols-4 lg:grid-rows-1"
      >
        <li
          v-for="item in recommendList"
          :key="item.id"
          class="flex rounded-xl flex-col items-center justify-between bg-white dark:bg-black w-54 h-80 gap-1 cursor-pointer hover:scale-105 duration-500 ease-in-out"
        >
          <a class="w-42" :href="`/introduction/${item.id}`">
            <img
              class="w-42 h-56 object-fit mt-5"
              :src="item.cover"
              :alt="item.title"
            />
            <div class="w-full h-12 flex flex-col justify-between items-center">
              <p class="truncate w-[90%] text-center">{{ item.title }}</p>
              <p class="truncate w-[90%] text-center text-sm text-gray-500">
                {{ item.author }}
              </p>
            </div>
          </a>
        </li>
      </ul>
    </section>
    <section class="h-20"></section>

  </main>
</template>

<script setup>
import request from "@/utils/request.js";
import { onBeforeMount, ref } from "vue";

const recommendList = ref([]);
const shelfBooks = ref([]);

onBeforeMount(() => {
  request.getRecommends().then((result) => {
    recommendList.value = result;
  });
  request
    .getHomeShelfBooks(1)
    .then((result) => {
      shelfBooks.value = result;
    })
    .catch((error) => {
      console.log("Get shelf books error: ", error);
    });
});

const changeBatch = () => {
  request.getRecommends().then((result) => {
    recommendList.value = result;
  });
};
</script>

<style lang="less" scoped>
.home-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  background: var(--color-background-light);
  padding-top: 1rem;
  padding-inline: var(--bg-padding);
}

.menu-list {
  display: flex;
  justify-content: center;
  .menu-wrapper {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(2, 1fr);
    gap: 10px;
    .menu-item {
      text-align: center;
      padding: 20px;
    }
  }
}

.recommend-list {
  margin-block: 2rem;
  display: flex;
  flex-direction: column;
  .recommend-title {
    display: flex;
    justify-content: start;
    align-items: center;
    line-height: 30px;
    .change {
      color: #969696;
      font-size: 14px;
      margin-left: 12px;
    }
  }
}

.content-list {
  width: 100%;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-block: 1rem;
  .book-card {
    width: 228px;
    height: 320px;
    margin: 12px;
    padding: 10px;
    background: var(--color-background-pure);
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    &:hover {
      transform: scale(1.05);
      transition: transform 0.5s ease;
    }
    img {
      width: 120px;
      height: 180px;
      margin-block: 20px;
    }
    .book-author {
      color: var(--color-little-word);
    }
  }
}
</style>
