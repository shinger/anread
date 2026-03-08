<template>
  <div
    class="absolute top-0 left-0 right-0 bottom-0 flex"
    v-show="message.length > 0"
  >
    <div class="m-auto p-2 bg-black/70 dark:bg-white text-white dark:text-black rounded-md">
      {{ message }}
    </div>
  </div>
</template>

<script setup>
import { useUtilStore } from "@/stores/util-store";
import { computed } from "vue";

const utilStore = useUtilStore();
const message = computed(() => utilStore.message);
</script>

<style></style>
