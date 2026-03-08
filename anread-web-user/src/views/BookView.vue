<script setup>
import Epub, { Book, EpubCFI, Layout, Rendition } from "epubjs";
import Themes from "epubjs/src/themes";
import request from "@/utils/request.js";
import {
  onBeforeMount,
  onMounted,
  ref,
  onBeforeUnmount,
  onUnmounted,
  watch,
} from "vue";
import { useRoute, useRouter, onBeforeRouteLeave } from "vue-router";
import { useThemeStore } from "@/stores/theme-store.js";
import { useBookStore } from "@/stores/book-store.js";
import { ReadingStatus } from "@/utils/enums.js";
import {
  renderEpub,
  setHighLight,
  book,
  rendition,
  tocList,
  locations,
  fonts,
  screenSize,
  readingRecord,
  highLightTocIndex,
} from "@/utils/epub-render.js";
import PenIcon from "@/components/icons/PenIcon.vue";
import FontIcon from "@/components/icons/FontIcon.vue";
import ThemeIcon from "@/components/icons/ThemeIcon.vue";
import MenuIcon from "@/components/icons/MenuIcon.vue";

const router = useRouter();
const route = useRoute();
const openSetting = ref(0);
const showDot = ref(2);
const bookInformation = ref(null);
const startTime = ref(null);
const endTime = ref(null);
const totalDuration = ref();
const timerIsActive = ref(true);
const finishedPage = ref(false);
const readFinished = ref(false);
const themeStore = useThemeStore();
const bookStore = useBookStore();
const epubURL = ref(null);
const annotations = ref([]);

// 页面离开前调用
const beforeLeave = () => {
  // 获取当前阅读位置
  let location = rendition.value.currentLocation();
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

  const progress = locations.value.percentageFromCfi(location.start.cfi);
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

// 路由守卫
onBeforeRouteLeave((to, from) => {
  if (from.path === route.path) {
    beforeLeave();
  }
  next();
});

onBeforeMount(() => {
  // 添加页面时间监听
  window.addEventListener("beforeunload", beforeLeave);
  window.addEventListener("unload", beforeLeave);

  // 记录阅读开始时间
  startTime.value = new Date().getTime();
  window.addEventListener("visibilitychange", handleVisibilityChange);

  // 获取字体列表
  request.getFonts().then((res) => {
    fonts.value = res;
  });

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
  window.addEventListener("resize", () => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
      // 销毁 EPUB 对象
      if (rendition.value) {
        rendition.value.destroy();
      }
      if (book.value) {
        book.value = null;
      }
      renderEpub(epubURL.value);
    }, 1000);
  });
});

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

onBeforeUnmount(() => {
  // 移除事件监听
  window.removeEventListener("beforeunload", beforeLeave);
  window.removeEventListener("unload", beforeLeave);

  // 销毁 EPUB 对象
  if (rendition.value) {
    rendition.value.destroy();
  }
  if (book.value) {
    book.value = null;
  }
});

// 切换字体
const changeFontSize = (i) => {
  showDot.value = i;
  rendition.value.themes.fontSize(12 + i * 2 + "px");
};

const changeFont = (fontValue) => {
  console.log("change font: ", fontValue);
  rendition.value.themes.font(fontValue);
  rendition.value.hooks.content.register((contents) => {
    contents.addStylesheetRules(
      {
        ".calibre8, .x4, .x9": {
          "font-family": `${fontValue} !important`,
        },
      },
      "inner-fonts"
    );
  });
};

// 切换目录
const changeToc = (item) => {
  rendition.value.display(item.href).then(() => {
    const location = rendition.value.currentLocation();
    setHighLight(location);
  });
};

// 设置
const onSetting = (i) => {
  switch (i) {
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
      if (themeStore.themeLight) {
        rendition.value.themes.select("theme-light");
      } else {
        rendition.value.themes.select("theme-dark");
      }
      break;
    default:
      break;
  }
};

// 上一页
const preview = () => {
  if (finishedPage.value) {
    finishedPage.value = false;
    return;
  }
  rendition.value.prev().then(() => {
    const location = rendition.value.currentLocation();
    setHighLight(location);
  });
};

// 下一页
const next = () => {
  rendition.value.next().then(() => {
    setHighLight(rendition.value.currentLocation());
  });

  // 最后一页的逻辑
  const locationLength = locations.value.length();
  const lastEpubCfi = locations.value._locations[locationLength - 1].replace(
    /,(.*?),/,
    ""
  );
  if (lastEpubCfi == rendition.value.currentLocation().end.cfi) {
    finishedPage.value = true;
  }
};

const markFinished = () => {
  request.markFinished(route.params.id).then((result) => {
    readFinished.value = true;
    router.push({ path: "/bookshelf" });
  });
};
</script>

<template>
  <main class="w-full h-[calc(100vh-120px)] p-2 sm:flex sm:justify-center">
    <!-- 阅读区域 -->
    <div
      class="h-full sm:block sm:relative sm:w-4/5 sm:rounded-2xl sm:bg-white dark:bg-black sm:mt-4"
    >
      <div class="w-full h-full relative">
        <div id="read" class="w-full h-full sm:pl-10"></div>
        <!-- 工具页 -->
        <div
          id="setting-wrapper"
          class="z-10 w-78 hidden sm:block sm:absolute right-0 top-0 bottom-0 bg-white dark:bg-black border border-gray-300 rounded-2xl overflow-y-scroll p-4"
          v-show="openSetting"
        >
          <!-- 目录 -->
          <div v-show="openSetting == 1">
            <a
              class="flex cursor-pointer"
              v-if="bookInformation"
              :href="`/introduction/${route.params.id}`"
            >
              <img
                class="w-15 h-22 object-cover mr-3"
                :src="bookInformation.cover"
              />
              <div class="flex flex-col justify-between items-center">
                <p class="text-lg font-bold">{{ bookInformation.title }}</p>
                <p class="text-base">{{ bookInformation.author }}</p>
              </div>
            </a>

            <nav class="flex flex-col items-start">
              <ul
                class="border-t border-gray-300 text-base/12 w-60 truncate"
                v-for="item in tocList"
                :key="item.id"
              >
                <li :class="{ highLight: item.highLight == true }">
                  <a @click="changeToc(item)">{{ item.label }}</a>
                </li>
                <li
                  class="pl-3 border-t border-gray-300 text-sm/10 h-10 w-60 truncate"
                  :class="{ highLight: subItem.highLight == true }"
                  v-for="subItem in item.subitems"
                  :key="subItem.id"
                >
                  <a @click="changeToc(subItem)">{{ subItem.label }}</a>
                </li>
              </ul>
            </nav>
          </div>
          <!-- 字体 -->
          <div class="flex flex-col cursor-pointer" v-show="openSetting == 2">
            <span>字号大小</span>
            <div
              class="self-center flex justify-between bg-gray-100 rounded-2xl w-full h-8 cursor-pointer"
            >
              <div class="z-5 mx-2 text-sm text-gray-400 leading-8 select-none">
                A
              </div>
              <div class="z-5 mx-2 text-sm text-gray-400 leading-8 select-none">
                A
              </div>
            </div>
            <div class="absolute -top-8 h-8 w-8 z-20 cursor-pointer"></div>
            <div class="z-2 relative left-0 w-full h-8 rounded-2xl">
              <div
                class="absolute -top-8 h-8 w-8 z-20 cursor-pointer"
                v-for="i in 6"
                :key="i"
                :style="`margin-left:${(i - 1) * 50}px;`"
                @click="changeFontSize(i)"
              ></div>
              <div
                class="absolute -top-8 h-8 rounded-2xl bg-gray-300 flex justify-end items-end p-1 cursor-pointer"
                v-for="i in 6"
                :key="i"
                :style="`width:${(i - 1) * 50 + 32}px;`"
                v-show="i == showDot"
              >
                <div class="bg-white rounded-full w-6 h-6"></div>
              </div>
              <ul class="flex flex-col justify-start my-4">
                <li
                  class="m-1"
                  v-for="font in fonts"
                  :key="font.id"
                  @click="changeFont(font.fontValue)"
                >
                  <a>{{ font.fontName }}</a>
                </li>
              </ul>
            </div>
          </div>
          <!-- 笔记 -->
          <div v-show="openSetting == 3">
            <ul>
              <li></li>
            </ul>
          </div>
        </div>
        <div
          class="absolute top-0 z-4 w-full h-full rounded-2xl bg-white dark:bg-black flex flex-col items-center justify-center text-xl"
          v-show="finishedPage"
        >
          <p>已读完</p>
          <button
            v-if="!readFinished"
            class="text-sm w-15 h-8 my-2"
            @click="markFinished"
          >
            标记读完
          </button>
        </div>
        <!-- 翻页按钮 -->
        <div class="hidden sm:block relative bottom-24 w-full z-6">
          <div class="p-12 flex justify-between">
            <button
              class="w-16 h-8 border rounded-xl bg-white dark:bg-black border-gray-300 dark:border-gray-600"
              @click="preview"
            >
              &lt; prev
            </button>
            <button
              class="w-16 h-8 border rounded-xl bg-white dark:bg-black border-gray-300 dark:border-gray-600"
              @click="next"
            >
              next &gt;
            </button>
          </div>
        </div>
        
      </div>

      <!-- 工具栏 -->
        <div class="hidden sm:block absolute top-45 -right-20">
          <div class="flex flex-col items-center justify-between w-16 h-64">
            <!-- 目录 -->
            <button
              class="p-2.5 rounded-full border border-gray-100 bg-white dark:bg-black dark:border-gray-500 shadow-sm"
              @click="onSetting(1)"
            >
              <a>
                <MenuIcon
                  size="20"
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
                  size="20"
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
                  size="20"
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
                  size="20"
                  :theme="themeStore.themeLight ? 'light' : 'dark'"
                />
              </a>
            </button>
          </div>
        </div>
    </div>
    <!-- 遮罩 -->
    <div
      class="fixed top-0 left-0 bottom-0 right-0 bg-black/20 z-5"
      v-show="openSetting > 0"
      @click="openSetting = 0"
    ></div>
  </main>
</template>

<style scoped>
#setting-wrapper::-webkit-scrollbar {
  border: none;
  width: 0;
  height: 0;
}

#setting-wrapper::-webkit-scrollbar-thumb {
  border-radius: 2px;
  -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0.2);
  background: #c7c7c7;
}

.highLight a {
  color: #c28e32;
}
</style>
