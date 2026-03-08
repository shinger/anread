<script setup>
import Themes from "epubjs/src/themes";
import request from "@/utils/request.js";
import {
  onBeforeMount,
  onMounted,
  ref,
  onBeforeUnmount,
  onUnmounted,
  nextTick,
} from "vue";
import { useRoute, useRouter, onBeforeRouteLeave } from "vue-router";
import { useThemeStore } from "@/stores/theme-store.js";
import { useBookStore } from "@/stores/book-store.js";
import { useUtilStore } from "@/stores/util-store.js";
import { ReadingStatus } from "@/utils/enums.js";
import ReadArea from "@/components/ReadArea.vue";
import PenIcon from "@/components/icons/PenIcon.vue";
import FontIcon from "@/components/icons/FontIcon.vue";
import ThemeIcon from "@/components/icons/ThemeIcon.vue";
import MenuIcon from "@/components/icons/MenuIcon.vue";
import CopyIcon from "@/components/icons/CopyIcon.vue";
import HighlightIcon from "@/components/icons/HighlightIcon.vue";
import IdeaIcon from "@/components/icons/IdeaIcon.vue";
import Avatar from "@/components/Avatar.vue";
import LikeIcon from "@/components/icons/LikeIcon.vue";
import CommentIcon from "@/components/icons/CommentIcon.vue";
import { getPublicIdeas } from "@/api/user-api.js";

const router = useRouter();
const route = useRoute();
const openSetting = ref(0);
const bookInformation = ref(null);
const startTime = ref(null);
const endTime = ref(null);
const totalDuration = ref();
const timerIsActive = ref(true);
const themeStore = useThemeStore();
const bookStore = useBookStore();
const readingRecord = ref(null);
const readFinished = ref(false);
const epubURL = ref(null);
const readAreaRef = ref(null);
const popupRef = ref(null);
const currentIdeas = ref([]);
const showIdeaList = ref(false);
const showMask = ref(false);
const utilStore = useUtilStore();

const renderEpub = (url) => {
  nextTick(() => {
    readAreaRef.value.renderEpub(url);
  });
};

// 路由守卫
onBeforeRouteLeave((to, from) => {
  if (from.path === route.path) {
    beforeLeave();
  }
  // next();
});

onBeforeMount(() => {
  // 添加页面时间监听
  window.addEventListener("beforeunload", beforeLeave);
  window.addEventListener("unload", beforeLeave);

  // 记录阅读开始时间
  startTime.value = new Date().getTime();
  window.addEventListener("visibilitychange", handleVisibilityChange);

  // 发起请求获取书本信息
  request.getBookInfo(route.params.id).then((result) => {
    bookInformation.value = result;
    bookStore.setBookInfo(result);
    if (result.readingStatus == ReadingStatus.READOVER) {
      readFinished.value = true;
    }
    epubURL.value = result.epubURL;
    renderEpub(result.epubURL);
  });

  request.getReadingRecord(route.params.id).then((result) => {
    readingRecord.value = result;
    if (result.readingStatus == ReadingStatus.READOVER) {
      readFinished.value = true;
    }
  });
});

let debounceTimer;
onMounted(() => {
  // 监听窗口大小变化，重新渲染epub
  window.addEventListener("resize", () => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
      readAreaRef.value.destroyEpub();
      renderEpub(epubURL.value);
    }, 1000);
  });
});

// 页面离开前调用，统计阅读时长
const beforeLeave = () => {
  // 获取当前阅读位置
  let location = readAreaRef.value.getCurrentLocation();
  let cfiString = location.start.cfi;
  console.log(cfiString);

  // 记录结束时间
  endTime.value = new Date().getTime();
  if (timerIsActive.value) {
    // totalDuration.value = (endTime.value - startTime.value) / 60000;
    totalDuration.value = (endTime.value - startTime.value) / 1000;
  }
  console.log("本次阅读时长(Min)：", Math.floor(totalDuration.value));
  window.removeEventListener("visibilitychange", handleVisibilityChange);

  const progress = readAreaRef.value.getProgress();
  request
    .uploadReadingRecord({
      bookId: route.params.id,
      lastReadCfi: cfiString,
      readingProgress: progress.toFixed(4) * 100,
      readingDuration: Math.floor(totalDuration.value),
    })
    .then((result) => {
      console.log(result);
    });
};

// 处理可见性变化
const handleVisibilityChange = () => {
  if (document.visibilityState === "hidden") {
    timerIsActive.value = false;
    totalDuration.value += new Date().getTime() - startTime.value;
  } else {
    timerIsActive.value = true;
    startTime.value = new Date().getTime();
  }
};

// 设置
const onSetting = (i) => {
  if (i > 0) {
    showMask.value = true;
  }
  switch (i) {
    case 0:
      showMask.value = false;
      break;
    case 1:
    case 2:
    case 3:
      if (openSetting.value == i) {
        openSetting.value = 0;
        return;
      }
      openSetting.value = i;
      break;
    case 4:
      themeStore.setTheme(!themeStore.themeLight);
      readAreaRef.value.refreshBookTheme();
      break;
    default:
      break;
  }
};

const cancelMask = () => {
  showMask.value = false;
  openSetting.value = 0;
  showIdeaList.value = false;
};

const showPopup = (pos) => {
  popupRef.value.style.left = pos.x + "px";
  popupRef.value.style.top = pos.y + "px";
  popupRef.value.classList.remove("hidden");
};

const openIdea = (data) => {
  console.log(data);
  getPublicIdeas(data.bookId, data.cfi).then((result) => {
    currentIdeas.value = result;
    showIdeaList.value = true;
    showMask.value = true;
  });
};

const copyLineContent = () => {
  navigator.clipboard.writeText(currentIdeas.value[0].lineContent).then(() => {
    cancelMask();
    utilStore.showMessage("已复制到剪贴板");
  });
};
</script>

<template>
  <main class="w-full h-[calc(100vh-120px)] p-2 sm:flex sm:justify-center">
    <!-- 阅读区域 -->
    <div
      class="h-full sm:block sm:relative sm:w-4/5 sm:rounded-2xl sm:bg-white dark:bg-black sm:mt-4"
    >
      <ReadArea
        ref="readAreaRef"
        :openSetting="openSetting"
        :bookInformation="bookInformation"
        @popup="showPopup"
        @hidePop="popupRef.classList.add('hidden')"
        @openIdea="openIdea"
      />
      <!-- 侧边选项栏 -->
      <div class="hidden sm:block absolute top-45 -right-20">
        <div class="flex flex-col items-center justify-between w-16 h-64">
          <!-- 目录 -->
          <button
            class="p-2.5 rounded-full border border-gray-100 bg-white dark:bg-black dark:border-gray-500 shadow-sm"
            @click="onSetting(1)"
          >
            <a>
              <MenuIcon
                :size="20"
                :theme="themeStore.themeLight ? 'light' : 'dark'"
              />
            </a>
          </button>
          <!-- 字体 -->
          <button
            class="p-2.5 rounded-full border border-gray-100 bg-white dark:bg-black dark:border-gray-500 shadow-sm"
            @click="onSetting(2)"
          >
            <a>
              <FontIcon
                :size="20"
                :theme="themeStore.themeLight ? 'light' : 'dark'"
              />
            </a>
          </button>
          <!-- 笔记 -->
          <button
            class="p-2.5 rounded-full border border-gray-100 bg-white dark:bg-black dark:border-gray-500 shadow-sm"
            @click="onSetting(3)"
          >
            <a
              ><PenIcon
                :size="20"
                :theme="themeStore.themeLight ? 'light' : 'dark'"
            /></a>
          </button>
          <!-- 主题 -->
          <button
            class="p-2.5 rounded-full border border-gray-100 bg-white dark:bg-black dark:border-gray-500 shadow-sm"
            @click="onSetting(4)"
          >
            <a>
              <ThemeIcon
                :size="20"
                :theme="themeStore.themeLight ? 'light' : 'dark'"
              />
            </a>
          </button>
        </div>
      </div>
    </div>

    <!-- 遮罩 -->
    <div
      class="fixed top-0 left-0 bottom-0 right-0 bg-black/20 z-10"
      v-show="showMask"
      @click="cancelMask()"
    ></div>

    <!-- 操作弹窗 -->
    <div
      ref="popupRef"
      class="absolute hidden bg-white dark:bg-black rounded-lg shadow-md p-0.5 z-100 min-w-[90px]"
    >
      <div
        @click="readAreaRef.handleCopy()"
        class="popup-item flex items-center gap-2 px-4 py-2.5 text-sm text-gray-800 dark:text-white cursor-pointer rounded transition-colors hover:bg-gray-100 dark:hover:bg-gray-700 active:bg-gray-200"
      >
        <CopyIcon />
        复制
      </div>

      <div
        @click="readAreaRef.handleHighlight()"
        class="popup-item flex items-center gap-2 px-4 py-2.5 text-sm text-gray-800 dark:text-white cursor-pointer rounded transition-colors hover:bg-gray-100 dark:hover:bg-gray-700 active:bg-gray-200"
      >
        <HighlightIcon />
        高亮
      </div>

      <div
        @click="readAreaRef.handleIdeaEdit()"
        class="popup-item flex items-center gap-2 px-4 py-2.5 text-sm text-gray-800 dark:text-white cursor-pointer rounded transition-colors hover:bg-gray-100 dark:hover:bg-gray-700 active:bg-gray-200"
      >
        <IdeaIcon />
        想法
      </div>
    </div>

    <!-- 想法列表 -->
    <div
      v-show="showIdeaList"
      class="absolute top-40 left-1/2 -translate-x-1/2 rounded-lg bg-[#f4f5f7] w-120 pb-2 z-15"
    >
      <div class="flex flex-col justify-center px-4 pt-4">
        <div class="text-lg font-bold text-[#24292f] text-center">公开想法</div>
        <div
          class="flex justify-center items-center bg-white rounded-lg px-2 mt-4"
        >
          <div class="w-80 h-16 flex justify-between items-center">
            <div
              @click="copyLineContent()"
              class="flex flex-col items-center hover:bg-[#f4f5f7] cursor-pointer w-14 h-14 rounded-lg py-2"
            >
              <CopyIcon :size="20" /><span class="text-[#353c46] py-1"
                >复制</span
              >
            </div>
            <div
              @click="
                readAreaRef.handleHighlightIdea(
                  currentIdeas[0].epubCfiRange,
                  currentIdeas[0].lineContent
                );
                cancelMask();
              "
              class="flex flex-col items-center hover:bg-[#f4f5f7] cursor-pointer w-14 h-14 rounded-lg py-2"
            >
              <HighlightIcon :size="20" /><span class="text-[#353c46] py-1"
                >划线</span
              >
            </div>
            <div
              @click="
                readAreaRef.handleIdeaEdit(currentIdeas[0]);
                cancelMask();
              "
              class="flex flex-col items-center hover:bg-[#f4f5f7] cursor-pointer w-14 h-14 rounded-lg py-2"
            >
              <IdeaIcon :size="20" /><span class="text-[#353c46] py-1"
                >写想法</span
              >
            </div>
          </div>
        </div>
        <div v-for="idea in currentIdeas" :key="idea.id" class="mt-4">
          <div class="bg-white rounded-lg p-4 cursor-pointer">
            <div class="flex items-center">
              <Avatar
                :size="32"
                :src="idea.avatar"
                :theme="themeStore.themeLight ? 'light' : 'dark'"
              />
              <span class="text-[#353c46] ml-2">{{ idea.username }}</span>
            </div>
            <div class="text-[#353c46] mt-2 border-b border-gray-300 p-2">
              {{ idea.ideaContent }}
            </div>
            <div class="flex justify-center items-center mt-2">
              <div class="flex items-center justify-between w-60">
                <LikeIcon :size="24" />
                <CommentIcon :size="24" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style></style>
