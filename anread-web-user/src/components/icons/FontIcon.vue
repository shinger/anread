<template>
  <i class="icon-font flex items-center justify-center">
    <svg
      :width="size"
      :height="size"
      viewBox="0 0 48 48"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M4 8H32"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
        stroke-linecap="round"
      />
      <path
        d="M28 21H44"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
        stroke-linecap="round"
      />
      <path
        d="M18 42L18 8"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
        stroke-linecap="round"
      />
      <path
        d="M36 42L36 21"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
        stroke-linecap="round"
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
