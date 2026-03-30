<template>
  <main class="w-full h-full relative flex flex-col">
    <div class="h-145">
      <ElABubbleList ref="bubbleListRef" class="scroll-area">
        <ElABubble v-for="(item, index) in list" :key="index" v-bind="item" footer-trigger="hover">
          <template #footer>
            <div class="actions" :class="item.placement">
              <span class="element-ai-vue-iconfont icon-copy"></span>
              <span class="element-ai-vue-iconfont icon-regen"></span>
            </div>
          </template>
        </ElABubble>
      </ElABubbleList>
    </div>

    <div
      class="h-[1/5] flex items-center justify-center"
    >
      <div
        class="w-full h-12 bg-white p-4 shadow-md rounded-md flex items-center justify-between"
      >
        <input type="text" class="w-full h-8 focus:outline-none" />
        <button class="rounded-full bg-gray-300 text-white" @click="sendMessage">
          <ArrowUpIcon />
        </button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ElABubbleList, ElABubble } from 'element-ai-vue'
import ArrowUpIcon from "@/components/icons/ArrowUpIcon.vue";
import { onMounted, ref, useTemplateRef } from 'vue'

const bubbleListRef = useTemplateRef('bubbleListRef')
const list = ref([
  {
    content: '你好我是element-ai-vue',
    placement: 'end',
    isMarkdown: false,
    loading: false,
    typing: false,
  },
])

const start = () => {
  const role = list.value.length % 2 === 0 ? 'user' : 'assistant'
  list.value.push({
    content: '你好我是element-ai-vue',
    placement: role === 'user' ? 'end' : 'start',
    isMarkdown: role === 'assistant',
    typing: role === 'assistant',
    variant: role === 'assistant' ? 'borderless' : 'default',
  })
}
const sendMessage = () => {
  list.value.push({
    content: '你好我是element-ai-vue',
    placement: 'end',
    isMarkdown: false,
    typing: false,
    variant: 'default',
  })
}
onMounted(() => {
  for (let i = 0; i < 10; i++) {
    start()
  }
//   bubbleListRef?.scrollToBottom()
})
</script>

<style scoped lang="less">
.scroll-area {
  padding: 10px;
}
.base-list {
  height: 400px;
  padding: 8px;
  box-sizing: border-box;
  .lite-item {
    line-height: 50px;
  }
}
.actions {
  display: flex;
  gap: 8px;
  font-size: 16px;
  &.end {
    justify-content: flex-end;
  }
  & > span {
    cursor: pointer;
    padding: 0 5px;
  }
}
</style>