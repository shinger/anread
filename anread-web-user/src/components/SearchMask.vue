<template>
  <div
    class="fixed top-0 left-0 bottom-0 right-0 z-5 lightMask"
    v-show="utilStore.mask && utilStore.specifyMask === 'search'"
    @click="utilStore.hideMask()"
  >
    <div class="w-full flex justify-center mt-6">
      <div class="w-[90%] bg-white rounded-2xl md:w-[70%]">
        <ul class="w-full flex flex-col justify-center gap-4 py-2" v-if="searchStore.getSearchResult().length != 0">
          <li v-for="(item,index) in searchStore.getSearchResult()" :key="item.id" class="border-gray-300 w-full" :class="{'border-b': index != searchStore.getSearchResult().length - 1}">
            <a :href="`/introduction/${item.id}`" class="w-full h-full flex items-center px-4 py-2 hover:bg-gray-100 rounded-lg">
              <img
                :src="item.cover"
                :alt="item.title"
                class="w-16 h-24 ml-4 object-fit"
              />
              <div class="flex flex-col justify-center ml-4">
                <p class="text-md text-black">{{ item.title }}</p>
                <p class="text-xs text-gray-500 mt-2">{{ item.author }}</p>
              </div>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUtilStore } from "@/stores/util-store";
import {useSearchStore} from "@/stores/search-store";
import { onMounted } from "vue";
const utilStore = useUtilStore();
const searchStore = useSearchStore();

onMounted(() => {
  utilStore.hideMask();
});
</script>

<style>
.lightMask {
  background-color: rgba(255, 255, 255, 0.55);
  backdrop-filter: blur(30px);
  -webkit-backdrop-filter: blur(30px);
  border: solid rgba(255, 255, 255, 0.18);
  box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
  -webkit-box-shadow: rgba(142, 142, 142, 0.19) 0px 6px 15px 0px;
  /* border-radius: 12px; */
  /* -webkit-border-radius: 12px; */
  color: rgb(255, 255, 255);
}
</style>
