<template>
  <i class="icon-font flex items-center justify-center">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlns:xlink="http://www.w3.org/1999/xlink"
      viewBox="0 0 12 12"
      :width="size"
      :height="size"
    >
      <g fill="none">
        <path
          d="M6 10.5a.5.5 0 0 0 .5-.5V3.207l2.646 2.647a.5.5 0 1 0 .708-.708l-3.5-3.5a.5.5 0 0 0-.708 0l-3.5 3.5a.5.5 0 1 0 .708.708L5.5 3.207V10a.5.5 0 0 0 .5.5z"
          :fill="themeStore.theme === 'light' ? 'black' : 'white'"
        ></path>
      </g>
    </svg>
  </i>
</template>

<script setup>
import { computed } from "vue";
import { useThemeStore } from "@/stores/theme-store.js";

const themeStore = useThemeStore();

const props = defineProps({
  size: {
    type: Number,
    default: 24,
  },
  theme: {
    type: String,
    default: "light",
  },
});

const size = computed(() => props.size);
</script>

<style></style>
