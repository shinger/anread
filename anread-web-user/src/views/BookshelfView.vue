<script setup>
import FileUpload from "@/components/FileUpload.vue";
import CheckIcon from "@/components/icons/CheckIcon.vue";
import { onMounted, onUnmounted, ref } from "vue";
import request from "@/utils/request.js";
import { removeBatchShelf } from "@/api/book-api.js";
import { useRouter } from "vue-router";

const books = ref([]);
const pageNum = ref(1);
const PAGE_SIZE = 30;
const isChecking = ref(false);
const selectedBookIds = ref([]);
const router = useRouter();

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

const handleSelect = (bookId) => {
  console.log(bookId);
  if (selectedBookIds.value.includes(bookId)) {
    selectedBookIds.value = selectedBookIds.value.filter((id) => id !== bookId);
  } else {
    selectedBookIds.value.push(bookId);
  }
};

const handleRemove = async () => {
  console.log(selectedBookIds.value);
  await removeBatchShelf(selectedBookIds.value);
  loadBooks();
  selectedBookIds.value = [];
  isChecking.value = false;
  router.push("/bookshelf");
};
</script>

<template>
  <main class="w-full px-5">
    <div class="flex justify-between items-center mt-4" v-show="!isChecking">
      <p class="text-xl font-bold">书架</p>
      <CheckIcon size="24" fixing @click="isChecking = true" />
    </div>
    <div class="flex justify-between items-center mt-4" v-show="isChecking">
      <button>全选</button>
      <p class="text-xl font-bold">选择书籍</p>
      <button @click="isChecking = false">取消</button>
    </div>
    <div class="flex justify-end" v-show="isChecking">
      <button @click="handleRemove" class="text-red-500">移除书架</button>
    </div>
    <div
      class="grid grid-cols-2 mt-4 sm:flex sm:flex-row sm:flex-wrap sm:justify-start"
    >
      <a
        :href="`/book/${book.id}`"
        class="mx-2 my-4 flex flex-col items-center justify-between sm:mx-8"
        v-for="book in books"
        :key="book.id"
      >
        <figure class="relative inline-block">
          <img :src="book.cover" alt="" class="w-36 h-50 object-fill" />
          <figcaption
            v-if="book.readFinished"
            class="absolute top-0 right-0 bg-[#e9a838] text-sm text-white px-2"
          >
            读完
          </figcaption>
          <CheckIcon
            class="absolute bottom-1 right-1 z-10"
            size="24"
            v-show="isChecking"
            @click="handleSelect(book.id)"
          />
          <span class="privateTag" v-if="book.isPrivate"></span>
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
.privateTag {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 28px;
  height: 28px;
  background: url("/private_tag.png")
    no-repeat;
  background-size: 100%;
}
</style>
