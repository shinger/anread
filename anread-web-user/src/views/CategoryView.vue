<template>
  <main class="min-w-full flex justify-start">
    <ul class="w-1/5 min-h-[calc(100vh-64px)] border-r-2 border-gray-300">
      <li
        class="w-full h-8 my-2 leading-8 text-sm text-center rounded-lg pl-2 cursor-pointer lg:text-lg font-bold lg:h-12 flex justify-end items-center"
        v-for="item in categoryList"
        :key="item.id"
        @click="changeCategory(item)"
      >
        <div class="w-full h-full mr-2 sm:w-28 sm:h-10" :class="{ activeTab: activeMainCategory === item }">
          <span class="lg:text-md">{{ item.categoryName }}</span>
        </div>
      </li>
    </ul>
    <div class="w-4/5">
      <div class="p-3 border-b-2 border-gray-300 flex flex-col">
        <h3>
          {{
            activeMainCategory == null ? "" : activeMainCategory.categoryName
          }}
        </h3>
        <ul
          class="mt-2 flex flex-wrap gap-4"
          v-if="subCategoryList.length != 0"
        >
          <li
            class="w-20 text-gray-600 cursor-pointer hover:text-black"
            @click="getAll"
            :class="{ active: isAll == true }"
          >
            全部
          </li>
          <li
            class="w-20 text-gray-600 cursor-pointer hover:text-black"
            v-for="item in subCategoryList"
            :key="item.id"
            @click="changeSubCategory(item)"
            :class="{ active: activeSubCategory === item }"
          >
            {{ item.categoryName }}
          </li>
        </ul>
      </div>
      <div class="mt-2">
        <div
          class="flex justify-center items-center px-4 py-2 cursor-pointer"
          v-for="item in currentBookList"
          :key="item.id"
          @click="toIntroPage(item.id)"
        >
          <img
            class="w-24 h-32 hover:scale-105 transition-all duration-300"
            :src="item.cover"
          />
          <div class="w-full pl-2">
            <h3 class="hover:text-[#b05f13]">{{ item.title }}</h3>
            <p class="text-sm text-gray-400 my-1">{{ item.author }}</p>
            <p class="text-sm text-gray-400 my-1">
              {{ item.readership }}人在读
            </p>
            <p class="text-sm text-gray-400 my-1">推荐值 92.3%</p>
            <p class="text-sm text-gray-400 hidden">
              {{ item.introduction }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { onBeforeMount, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import request from "@/utils/request.js";

const router = useRouter();
const categoryList = ref([]);
const subCategoryList = ref([]);
const currentBookList = ref([]);
const activeMainCategory = ref(null);
const activeSubCategory = ref(null);
const isAll = ref(true);

const toIntroPage = (id) => {
  console.log(id);
  router.push(`/introduction/${id}`);
};

const getAll = () => {
  isAll.value = true;
  activeSubCategory.value = null;
  request
    .getCategoryBooks(activeMainCategory.value.id)
    .then((result) => {
      currentBookList.value = result;
    })
    .catch((error) => {
      console.log(error);
    });
};

const changeCategory = (item) => {
  activeMainCategory.value = item;
  isAll.value = true;
  request
    .getCategories(item.id)
    .then((result) => {
      subCategoryList.value = result;
    })
    .catch((error) => {
      console.log(error);
    });
  getAll();
};

const changeSubCategory = (item) => {
  activeSubCategory.value = item;
  isAll.value = false;
  request
    .getCategoryBooks(item.id)
    .then((result) => {
      currentBookList.value = result;
    })
    .catch((error) => {
      console.log(error);
    });
};

let loaded = false;

onBeforeMount(() => {
  if (!loaded) {
    request
      .getCategories(0)
      .then((result) => {
        categoryList.value = result;
        changeCategory(result[0]);
      })
      .catch((error) => {
        console.log(error);
      });
    loaded = true;
  }
});
</script>

<style scoped>
.active {
  color: #df8e41 !important;
}

.activeTab {
  background-color: #fb8a1ee0;
  color: white !important;
  border-radius: 6px;
}
</style>
