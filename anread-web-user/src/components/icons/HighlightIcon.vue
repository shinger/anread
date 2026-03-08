<template>
  <i class="icon-font flex items-center justify-center">
    <svg
      :width="size"
      :height="size"
      viewBox="0 0 48 48"
      :fill="themeStore.themeLight ? '#000' : '#fff'"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M6 44L6 25H12V17H36V25H42V44H6Z"
        :fill="themeStore.themeLight ? '#fff' : '#000'"
        :stroke="themeStore.themeLight ? '#000' : '#fff'"
        stroke-width="3"
        stroke-linejoin="round"
      />
      <path
        d="M17 17V8L31 4V17"
        :fill="themeStore.themeLight ? '#fff' : '#000'"
        :stroke="themeStore.themeLight ? '#000' : '#fff'"
        stroke-width="3"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
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
