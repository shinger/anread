<template>
  <div
    class="avatar-wrapper"
    :style="{ width: props.size + 'px', height: props.size + 'px' }"
  >
    <img :src="props.src" alt="" />
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  src: String,
  size: {
    type: Number,
    default: 24,
  },
});
</script>

<style scoped>
.avatar-wrapper {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid #ccc;
  cursor: pointer;
  overflow: hidden;
}

.avatar-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
