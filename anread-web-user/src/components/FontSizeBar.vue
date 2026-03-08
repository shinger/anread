<template>
  <div>
    <div
      ref="sliderContainer"
      class="relative w-full h-6 bg-gray-300 rounded-xl my-5 cursor-pointer"
      @click="handleSliderClick"
    >
      <!-- Position markers -->
      <div
        v-for="(pos, index) in snapPositions"
        :key="index"
        class="absolute w-3 h-3 bg-gray-200 rounded-full top-1/2 -translate-y-1/2 -translate-x-1/2"
        :style="{ left: pos + '%' }"
      ></div>

      <!-- Slider Track -->
      <div
        class="absolute h-full bg-gray-400 rounded-xl transition-all duration-300"
        :style="{ width: value + '%' }"
      ></div>

      <!-- Slider Handle -->
      <!-- <div
        class="absolute w-6 h-6 bg-white rounded-full top-1/2 transition-all duration-300 z-10"
        :class="[
          isDragging
            ? 'cursor-grabbing scale-95 duration-100'
            : 'cursor-grab hover:scale-110',
        ]"
        :style="{
          left: value + '%',
          transform: 'translate(-50%, -50%)',
        }"
        @mousedown="handleMouseDown"
        @touchstart="handleTouchStart"
      ></div> -->
    </div>

    <div class="text-center mt-6 text-3xl font-bold text-indigo-600">
      {{ Math.round(value) }}%
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";

const sliderContainer = ref(null);
const value = ref(0);
const isDragging = ref(false);
const positions = ref(7);
const snapPositions = ref([0, 16.67, 33.33, 50, 66.67, 83.33, 100]);

const snapToNearestPosition = (percentage) => {
  let nearest = snapPositions.value[0];
  let minDiff = Math.abs(percentage - nearest);

  for (let pos of snapPositions.value) {
    const diff = Math.abs(percentage - pos);
    if (diff < minDiff) {
      minDiff = diff;
      nearest = pos;
    }
  }

  return nearest;
};

const updateSlider = (clientX) => {
  if (!sliderContainer.value) return;

  const rect = sliderContainer.value.getBoundingClientRect();
  let position = clientX - rect.left;

  // Constrain position within bounds
  position = Math.max(0, Math.min(position, rect.width));

  // Calculate percentage
  const percentage = (position / rect.width) * 100;
  value.value = percentage;
};

const handleMouseDown = (e) => {
  isDragging.value = true;
  e.preventDefault();
};

const handleMouseMove = (e) => {
  if (isDragging.value) {
    updateSlider(e.clientX);
  }
};
const handleMouseUp = () => {
  isDragging.value = false;
};
const handleTouchStart = (e) => {
  isDragging.value = true;
  e.preventDefault();
};
const handleTouchMove = (e) => {
  if (isDragging.value) {
    updateSlider(e.touches[0].clientX);
  }
};
const handleTouchEnd = () => {
  if (this.isDragging) {
    value.value = snapToNearestPosition(value.value);
  }
  isDragging.value = false;
};
const handleSliderClick = (e) => {
  const rect = sliderContainer.value.getBoundingClientRect();
  let position = e.clientX - rect.left;
  position = Math.max(0, Math.min(position, rect.width));
  const percentage = (position / rect.width) * 100;
  value.value = snapToNearestPosition(percentage);
};

onMounted(() => {
  document.addEventListener("mousemove", handleMouseMove);
  document.addEventListener("mouseup", handleMouseUp);
  document.addEventListener("touchmove", handleTouchMove);
  document.addEventListener("touchend", handleTouchEnd);
});

onBeforeUnmount(() => {
  document.removeEventListener("mousemove", handleMouseMove);
  document.removeEventListener("mouseup", handleMouseUp);
  document.removeEventListener("touchmove", handleTouchMove);
  document.removeEventListener("touchend", handleTouchEnd);
});
</script>

<style scoped>
.container {
  background: white;
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 500px;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.slider-container {
  position: relative;
  width: 100%;
  height: 8px;
  background: #e0e0e0;
  border-radius: 10px;
  margin: 20px 0;
  cursor: pointer;
}

.slider-track {
  position: absolute;
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  width: 0%;
  transition: width 0.1s ease;
}

.slider-handle {
  position: absolute;
  width: 24px;
  height: 24px;
  background: white;
  border: 3px solid #667eea;
  border-radius: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  cursor: grab;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.1s ease;
  left: 0%;
}

.slider-handle:hover {
  transform: translate(-50%, -50%) scale(1.1);
}

.slider-handle:active {
  cursor: grabbing;
  transform: translate(-50%, -50%) scale(0.95);
}

.value-display {
  text-align: center;
  margin-top: 20px;
  font-size: 24px;
  font-weight: bold;
  color: #667eea;
}
</style>
