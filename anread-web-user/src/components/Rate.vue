<template>
  <div :style="fontStyle">
    <div class="rate" @mouseout="mouseOut">
      <span class="star" @mouseover="mouseOver(num)" v-for="num in 5" :key="num"
        >☆</span
      >
      <span class="yellow" :style="fontWidth">
        <span
          class="star"
          @click="onRate(num)"
          @mouseover="mouseOver(num)"
          v-for="num in 5"
          :key="num"
          >★</span
        >
      </span>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps, defineEmits, ref, watch } from "vue";

const props = defineProps({
  score: {
    type: Number,
    default: 0,
  },
  theme: {
    type: String,
    default: "blue",
  },
  readOnly: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["update-score"]);

const themeObj = {
  black: "#000",
  yellow: "#fadb14",
  blue: "#40a9ff",
  green: "#73d13d",
};

// ✅ 新增：本地状态，用于跟踪当前显示的评分
const currentScore = ref(props.score);

// ✅ 监听 props.score 变化（例如父组件响应 emit 后更新了 score）
watch(
  () => props.score,
  (newScore) => {
    currentScore.value = newScore;
  }
);

const fontStyle = computed(() => {
  return `color: ${themeObj[props.theme]};`;
});

const onRate = (num) => {
  if (props.readOnly) return;
  currentScore.value = num; // ✅ 立即更新本地状态
  emits("update-score", num);
};

// ✅ 更清晰的做法：引入 hoverScore
const hoverScore = ref(0);

const mouseOver = (num) => {
  if (props.readOnly) return;
  hoverScore.value = num;
};

const mouseOut = () => {
  if (props.readOnly) return;
  hoverScore.value = 0; // 清除 hover，回到 currentScore
};

// 重写 fontWidth：hover 时用 hoverScore，否则用 currentScore
const fontWidth = computed(() => {
  const displayScore = hoverScore.value || currentScore.value;
  return `width: ${displayScore}em;`;
});
</script>

<style lang="css" scoped>
.star {
  letter-spacing: 3px;
}
.rate {
  position: relative;
  display: inline-block;
  font-size: 30px;
}
.rate > span.yellow {
  position: absolute;
  display: inline-block;
  top: 0;
  left: 0;
  overflow: hidden;
}
</style>