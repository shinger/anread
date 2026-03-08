<script setup>
import FileUpload from "@/components/FileUpload.vue";
import { onMounted, onUnmounted, ref } from "vue";
import request from "@/utils/request.js";

const books = ref([]);
const pageNum = ref(1);
const PAGE_SIZE = 30;

onMounted(() => {
  loadBooks();
  window.addEventListener("scroll", handleScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});

const loadBooks = async () => {
  await request
    .getShelfBooks(pageNum.value)
    .then((result) => {
      books.value = result;
    })
    .catch((error) => {
      console.log("Get shelf books error: ", error);
    });
};

// 滚动事件处理：判断是否滚动到底部
const handleScroll = () => {
  // 防抖：避免频繁触发
  clearTimeout(window.scrollTimer);
  window.scrollTimer = setTimeout(() => {
    const scrollTop =
      document.documentElement.scrollTop || document.body.scrollTop;
    const clientHeight = document.documentElement.clientHeight;
    const scrollHeight = document.documentElement.scrollHeight;

    // 滚动到底部附近（距离底部100px），触发加载更多
    if (scrollTop + clientHeight >= scrollHeight - 100) {
      console.log("loading more books...");
      loadBooks();
    }
  }, 500);
};
</script>

<template>
  <main class="w-full px-5">
    <div
      class="grid grid-cols-2 mt-12 sm:flex sm:flex-row sm:flex-wrap sm:justify-start"
    >
      <a
        :href="`/book/${book.id}`"
        class="mx-2 my-4 flex flex-col items-center justify-between sm:mx-8"
        v-for="book in books"
        :key="book.id"
      >
        <figure class="relative inline-block">
          <img :src="book.cover" alt="" class="w-36 h-50 object-fill" />
          <figcaption v-if="book.readFinished" class="absolute top-0 right-0 bg-[#e9a838] text-sm text-white px-2">读完</figcaption>
        </figure>
        <div class="mt-2 text-center w-36 truncate">{{ book.title }}</div>
      </a>
      <!-- <div href="" class="shelf-book">
        <FileUpload />
      </div> -->
    </div>
  </main>
</template>

<style lang="less" scoped>
/*
main {
  width: 100%;
  position: absolute;
  top: 0;
  padding-inline: var(--bg-padding);
}
.shelf-list {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  margin-top: 40px;
  .shelf-book {
    content-visibility: auto;
    position: relative;
    display: block;
    width: 128px;
    height: auto;
    margin-right: 36px;
    margin-bottom: 44px;
    .book-cover {
      width: 100%;
      height: 185px;
      img {
        vertical-align: top;
        width: 100%;
        height: 100%;
        background-color: rgb(216, 216, 216);
        object-fit: cover;
      }
    }
    .title {
      margin-top: 14px;
      font-size: 15px;
      line-height: 18px;
      text-align: center;
    }
  }
}*/
</style>
