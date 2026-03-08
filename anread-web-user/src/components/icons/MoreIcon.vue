<template>
  <i class="icon-font flex items-center justify-center">
    <svg
      :width="size"
      :height="size"
      xmlns="http://www.w3.org/2000/svg"
      xmlns:xlink="http://www.w3.org/1999/xlink"
      viewBox="0 0 1024 1024"
    >
      <path
        fill="currentColor"
        d="M456 231a56 56 0 1 0 112 0a56 56 0 1 0-112 0zm0 280a56 56 0 1 0 112 0a56 56 0 1 0-112 0zm0 280a56 56 0 1 0 112 0a56 56 0 1 0-112 0z"
      ></path>
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
