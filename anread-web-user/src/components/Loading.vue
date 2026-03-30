<template>
  <div class="flex justify-center items-center">
    <div class="loading-spinner"></div>
    <div class="loading-text">{{ message }}</div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const props = defineProps({
  message: {
    type: String,
    default: "加载中...",
  },
});
</script>

<style>
.loading-spinner {
  margin-top: 20px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  animation: spin 1s linear infinite;
}

.loading-text {
  margin-top: 10px;
  margin-left: 16px;
  font-size: 16px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>