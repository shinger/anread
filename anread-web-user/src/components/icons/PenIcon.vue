<template>
  <i class="icon-menu flex items-center justify-center">
    <svg
        :width="size"
        :height="size"
        viewBox="0 0 48 48"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
      <path
        d="M5.32497 43.4996L13.81 43.4998L44.9227 12.3871L36.4374 3.90186L5.32471 35.0146L5.32497 43.4996Z"
        :fill="theme === 'dark' ? 'white' : 'none'"
        stroke="#000000"
        stroke-width="3"
        stroke-linejoin="round"
      />
      <path
        d="M27.9521 12.3872L36.4374 20.8725"
        :fill="theme === 'dark' ? 'white' : 'none'"
        stroke="#000000"
        stroke-width="3"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
    </svg>
  </i>
</template>

<script setup>
import { computed } from "vue";

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
