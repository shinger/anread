<template>
  <div ref="testRef" class="container">
    长按选中这段文字，测试坐标是否正常
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const testRef = ref(null);
// 实时记录触摸坐标（核心：全程更新，不依赖单次事件）
let lastTouchX = 0;
let lastTouchY = 0;

// 1. 触摸开始：初始化坐标 + 开始实时监听
const handleTouchStart = (e) => {
  const touch = e.targetTouches[0];
  lastTouchX = touch.clientX;
  lastTouchY = touch.clientY;
  console.log("【触摸开始】", lastTouchX, lastTouchY);
};

// 2. 触摸移动：实时更新坐标（即使长按选中文本，touchmove仍会触发）
const handleTouchMove = (e) => {
  const touch = e.targetTouches[0];
  lastTouchX = touch.clientX;
  lastTouchY = touch.clientY;
  // 可选：打印移动过程坐标，验证实时更新
  // console.log("【触摸移动】", lastTouchX, lastTouchY);
};

// 3. 文本选择变化：直接用实时记录的坐标（不再依赖touchend）
const handleSelectionChange = () => {
  const selectedText = window.getSelection().toString().trim();
  if (selectedText) {
    console.log("【长按选中文本】最终坐标：", lastTouchX, lastTouchY);
  } else {
    console.log("【未选中文本】坐标：", lastTouchX, lastTouchY);
  }
};

// 4. 绑定所有触摸事件（覆盖全生命周期）
onMounted(() => {
  if (testRef.value) {
    // 绑定touchstart：初始记录
    window.addEventListener('touchstart', handleTouchStart);
    // 绑定touchmove：实时更新（关键！选中文本时仍会触发）
    // testRef.value.addEventListener('touchmove', handleTouchMove, { passive: true });
  }
  console.log(window.document)
  // 监听文本选择事件（触发时直接用最后记录的坐标）
  document.addEventListener('selectionchange', handleSelectionChange);
});

// 5. 卸载时移除事件
onUnmounted(() => {
  if (testRef.value) {
    testRef.value.removeEventListener('touchstart', handleTouchStart);
    testRef.value.removeEventListener('touchmove', handleTouchMove);
  }
  document.removeEventListener('selectionchange', handleSelectionChange);
});
</script>

<style scoped>
.container {
  width: 100vw;
  height: 200px;
  border: 1px solid #000;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  /* 保留文本选择能力，不做任何阻止 */
  user-select: text;
  -webkit-user-select: text;
}
</style>