<template>
  <i class="icon-font flex items-center justify-center" @click.prevent="handleCheckClick">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      :width="size"
      :height="size"
      viewBox="0 0 200 200"
    >
      <!-- 绘制圆圈 -->
      <circle
        cx="100"
        cy="100"
        r="80"
        :stroke="themeStore.themeLight ? '#000' : '#fff'"
        stroke-width="10"
        :fill="isChecked ? '#e9a838' : 'none'"
      />

      <!-- 对勾 -->
      <path
        d="M60 100L90 130L140 80"
        :stroke="themeStore.themeLight ? '#000' : '#fff'"
        stroke-width="10"
        fill="none"
        stroke-linecap="round"
        stroke-linejoin="round"
      />
    </svg>
  </i>
</template>

<script setup>
import { computed, ref } from "vue";
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
  fixing: {
    type: Boolean,
    default: false,
  }
});

const size = computed(() => props.size);

const isChecked = ref(false);

const handleCheckClick = () => {
  if (props.fixing) {
    return;
  }
  isChecked.value = !isChecked.value;
};
</script>

<style></style>
