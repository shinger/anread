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
        d="M7.94971 11.9497H39.9497"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
      <path
        d="M7.94971 23.9497H39.9497"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
      <path
        d="M7.94971 35.9497H39.9497"
        :stroke="theme === 'dark' ? '#f5f5f5' : '#000000'"
        stroke-width="4"
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
