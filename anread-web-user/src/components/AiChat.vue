<template>
  <main
    class="w-full h-full relative flex flex-col overflow-hidden pb-20"
    :class="{ 'h-28': qutation != '' }"
  >
    <div class="h-145 overflow-y-scroll" id="chat-wrapper">
      <ElABubbleList ref="bubbleListRef" class="scroll-area">
        <ElABubble
          v-for="(item, index) in chatRecords"
          :key="index + '_' + item.content.length"
          v-bind="item"
          footer-trigger="hover"
          :content="item.content"
          :placement="item.placement"
          :variant="item.variant"
          :loading="item.loading"
          :isMarkdown="item.isMarkdown"
          shape="corner"
          v-show="item.content != '' || item.loading"
        >
          <template v-if="true">
            <div class="flex flex-col justify-start">
              <div
                v-if="item.quotation != ''"
                class="w-55 h-full flex items-center justify-between text-gray-500 px-2 border-l-4 border-[#cacaca]"
              >
                <p class="truncate">{{ item.quotation }}</p>
              </div>
              <div class="flex items-center justify-start">
                <ErrorIcon v-if="item.error" size="16" class="mr-2" />

                <p>{{ item.content }}</p>
              </div>
            </div>
          </template>
          <template #footer>
            <div class="actions" :class="item.placement">
              <span
                class="element-ai-vue-iconfont icon-copy"
                @click="copy(index)"
              ></span>
              <span
                class="element-ai-vue-iconfont icon-regen"
                v-show="item.placement == 'start'"
                @click="regenerate(index)"
              ></span>
            </div>
          </template>
        </ElABubble>
      </ElABubbleList>
      <!-- <ElABubble :content="sendedMessage" placement="end" v-show="sendedMessage != ''"/>
        <ElABubble :content="answerText" variant="outlined" :loading="loading" v-show="loading || answerText != ''"/> -->
    </div>

    <div
      class="absolute bottom-0 left-0 right-0 h-12 flex flex-col"
      :class="{ 'h-18': qutation != '' }"
    >
      <div
        class="w-full h-12 bg-white dark:bg-[#222222] p-2 shadow-md rounded-md flex flex-col"
        :class="{ 'h-18': qutation != '' }"
      >
        <div
          v-show="qutation != ''"
          class="w-full h-6 flex items-center justify-between text-gray-500 px-2 border-l-4 bg-[#f7f7f8] border-[#cacaca]"
        >
          <p class="truncate">{{ qutation }}</p>
          <CloseIcon size="14" class="cursor-pointer" />
        </div>
        <div class="w-full h-8 flex items-center gap-2">
          <input
            type="text"
            class="w-full h-8 focus:outline-none"
            v-model="message"
            @keyup.enter="sendMessage(0)"
          />
          <button
            class="rounded-full text-white"
            @click="sendMessage(0)"
            :class="{
              'bg-amber-600': message != '',
              'bg-gray-300': message == '',
            }"
          >
            <ArrowUpIcon />
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ElABubbleList, ElABubble, createTypewriter } from "element-ai-vue";
import ArrowUpIcon from "@/components/icons/ArrowUpIcon.vue";
import { nextTick, onMounted, ref, useTemplateRef } from "vue";
import {
  getChatRecords,
  uploadChatRecord,
  updateChatRecord,
} from "@/api/book-api";
import { createReusableTemplate } from "@vueuse/core";
import { useUtilStore } from "@/stores/util-store.js";
import { useToast } from "vue-toast-notification";
import CloseIcon from "@/components/icons/CloseIcon.vue";
import ErrorIcon from "@/components/icons/ErrorIcon.vue";

const toast = useToast();
const [ReuseTemplate] = createReusableTemplate();
const utilStore = useUtilStore();
const props = defineProps({
  bookId: {
    type: String,
    default: "",
  },
});
const message = ref("");
const bubbleListRef = useTemplateRef("bubbleListRef");
const chatRecords = ref([]);
const qutation = ref("");

const sendMessage = async (index) => {
  const sendedMessage = message.value;
  const quotationText = qutation.value;
  let answerText = "";
  console.log("问题：", sendedMessage);
  message.value = "";
  qutation.value = "";

  if (index == 0) {
    await chatRecords.value.push({
      content: sendedMessage,
      placement: "end",
      isMarkdown: false,
      typing: false,
      variant: "default",
      quotation: quotationText,
    });
  }

  if (index > 0) {
    chatRecords.value[index].content = "";
    chatRecords.value[index].loading = true;
  } else {
    await chatRecords.value.push({
      content: "",
      placement: "start",
      isMarkdown: true,
      typing: false,
      variant: "outlined",
      loading: true,
      error: false,
    });
  }

  const answerIndex = index > 0 ? index : chatRecords.value.length - 1;

  const typewriter = createTypewriter({
    interval: 100,
  });
  typewriter.start((text) => {
    chatRecords.value[answerIndex].content = text;
  });

  try {
    const response = await fetch("http://localhost:8000/book-api/chat/rag", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token") || "",
      },
      body: JSON.stringify({
        bookId: props.bookId,
        message: sendedMessage,
        quotation: quotationText,
      }),
    });
    // const responseClone = response.clone();

    // let jsonResponse;
    // try {
    //   jsonResponse = await responseClone.json();
    // } catch (e) {
    //   console.error("非 JSON 响应：", await responseClone.text());
    //   throw new Error("服务器返回了非 JSON 格式的响应");
    // }

    // if (jsonResponse && jsonResponse.code == "500") {
    //   console.error("服务器错误：", jsonResponse.message);
    //   toast.error(jsonResponse.message || "服务器发生错误");
    // }

    const reader = response.body.getReader();
    const decoder = new TextDecoder();
    let buffer = "";

    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        console.log("✅ 最终完整结果：", answerText);
        if (index > 0) {
          updateChatRecord({
            id: chatRecords.value[index].id,
            answer: answerText,
          });
        } else {
          if (answerText.trim() == "") {
            // 没有生成内容，删除占位记录
            chatRecords.value.splice(answerIndex, 1);
            return;
          }
          // uploadChatRecord({
          //   bookId: props.bookId,
          //   question: sendedMessage,
          //   answer: answerText,
          // }).then((res) => {
          //   chatRecords.value[answerIndex].id = res;
          // });
        }
        break;
      }

      buffer += decoder.decode(value, { stream: true });
      const lines = buffer.split("\n");

      while (lines.length > 0) {
        let line = lines.shift();

        if (!line.trim()) continue;

        // 统一清洗 SSE 格式
        line = line.trimStart().replace(/^data:\s*/, "");

        // 结束标志
        if (line === "[DONE]") {
          buffer = "";
          break;
        }

        // 解析真实内容
        try {
          let content = line;
          if (isJson(line)) {
            const json = JSON.parse(line);
            if (json.code === "500") {
              console.error("服务器错误：", json.message);
              toast.error(json.message || "服务器发生错误");
              typewriter.addText("服务器发生错误");
              chatRecords.value[answerIndex].loading = false;
              chatRecords.value[answerIndex].error = true;
              return;
            }
            content = json.choices?.[0]?.delta?.content || "";
          }

          if (content) {
            chatRecords.value[answerIndex].loading = false;
            console.log("回答：", content);
            // await nextTick();
            typewriter.addText(content);
            answerText += content;
          }
        } catch (e) {}
      }

      buffer = lines.join("\n");
    }
  } catch (error) {
    console.error(error);
  } finally {
    chatRecords.value[answerIndex].loading = false;
    // typewriter.stop();
    // typewriter.destory();
  }
};

const regenerate = async (index) => {
  console.log("当前回答：", chatRecords.value[index]);
  console.log("上一个问题：", chatRecords.value[index - 1]);

  const record = chatRecords.value[index];
  if (!record || record.placement !== "start") return;

  const questionRecord = chatRecords.value[index - 1];
  if (!questionRecord || questionRecord.placement !== "end") return;

  // 删除当前回答
  chatRecords.value.splice(index, 1);
  // 删除当前问题
  chatRecords.value.splice(index - 1, 1);

  bubbleListRef.value?.scrollToBottom();

  // 重新发送问题
  message.value = questionRecord.content;
  await nextTick();
  sendMessage();
};

const scrollToBottomChat = async () => {
  console.log("滚动到最底部");
  await nextTick();
  bubbleListRef.value?.scrollToBottom();
};

onMounted(() => {
  getChatRecords(props.bookId).then((res) => {
    res.forEach((item) => {
      chatRecords.value.push({
        id: item.id,
        content: item.question,
        placement: "end",
        isMarkdown: false,
        typing: false,
        variant: "default",
        loading: false,
        quotation: item.quotation,
      });
      chatRecords.value.push({
        id: item.id,
        content: item.answer,
        placement: "start",
        isMarkdown: true,
        typing: false,
        variant: "outlined",
        loading: false,
      });
    });
  });
});

const copy = (index) => {
  const content = chatRecords.value[index].content;
  navigator.clipboard.writeText(content).then(() => {
    utilStore.showMessage("已复制到剪贴板");
  });
};

const prepareQutation = (content) => {
  qutation.value = content;
};

const isJson = (str) => {
  if (typeof str !== "string") {
    return false;
  }
  try {
    const parsed = JSON.parse(str);
    // 确保解析结果不是原始类型（如 number, boolean），除非你接受它们
    // 通常我们期望 JSON 是对象或数组
    return typeof parsed === "object" && parsed !== null;
  } catch (e) {
    return false;
  }
};

defineExpose({
  scrollToBottomChat,
  prepareQutation,
});
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

#chat-wrapper::-webkit-scrollbar {
  border: none;
  width: 0;
  height: 0;
}

#chat-wrapper::-webkit-scrollbar-thumb {
  border-radius: 2px;
  -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0.2);
  background: #c7c7c7;
}
</style>