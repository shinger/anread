<template>
  <div class="w-full h-full relative">
    <div id="read" class="w-full h-full sm:pl-10"></div>
    <!-- 侧边选项栏弹窗 -->
    <div
      id="setting-wrapper"
      class="z-50 w-78 hidden sm:block sm:absolute right-0 top-0 bottom-0 bg-gray-100 dark:bg-black border border-gray-300 rounded-2xl overflow-y-scroll p-4"
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
              <a @click="displayToHref(item.href)">{{ item.label }}</a>
            </li>
            <li
              class="pl-3 border-t border-gray-300 text-sm/10 h-10 w-60 truncate"
              :class="{ highLight: subItem.highLight == true }"
              v-for="subItem in item.subitems"
              :key="subItem.id"
            >
              <a @click="displayToHref(subItem.href)">{{ subItem.label }}</a>
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
      <div v-show="openSetting == 3" v-if="tocList.length">
        <ul class="flex flex-col">
          <li
            class="w-full flex flex-col p-2 mb-1"
            v-for="(annotation, index) in annotations"
            :key="index"
          >
            <p>
              {{
                getTocTitle(annotation.tocParentIndex, annotation.tocChildIndex)
              }}
            </p>
            <a
              @click="displayToHref(item.epubCfiRange)"
              class="w-full bg-white rounded-md flex flex-col justify-between p-2 my-2 text-sm"
              v-for="item in annotation.userAnnotationList"
              :key="item.id"
            >
              <div
                :title="item.lineContent"
                class="overflow-hidden text-ellipsis w-full max-h-40"
              >
                {{ item.lineContent }}
              </div>
              <div
                v-if="item.type == 'idea'"
                class="border-l-2 border-gray-300 pl-2 mt-2 text-sm text-gray-500"
              >
                {{ item.ideaContent }}
              </div>
            </a>
          </li>
        </ul>
      </div>
    </div>
    <!-- 已读完页面 -->
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
    <div
      class="absolute top-0 left-0 right-0 bottom-0 bg-black/50"
      v-show="isPopupShow"
    ></div>
    <!-- 想法编辑弹窗 -->
    <div
      v-show="showIdeaEditor"
      class="absolute top-1/2 left-1/2 -translate-x-1/2 w-160 border border-gray-300 dark:border-gray-600 bg-white dark:bg-black rounded-2xl p-4 z-10"
    >
      <div class="flex justify-end">
        <button
          class="rounded-full border border-gray-300 dark:border-gray-600 w-6 h-6 flex items-center justify-center p-1"
          @click="showIdeaEditor = false"
        >
          <CloseIcon
            :size="16"
            :theme="themeStore.themeLight ? 'light' : 'dark'"
          />
        </button>
      </div>
      <div class="flex mt-2">
        <span class="mt-0.5">
          <IdeaIcon
            :size="20"
            :theme="themeStore.themeLight ? 'light' : 'dark'"
          />
        </span>
        <textarea
          class="w-full h-48 px-2 border border-gray-300 dark:border-gray-600 rounded-md text-gray-800 dark:text-gray-200 bg-white dark:bg-black"
          placeholder="记录此刻的想法"
          v-model="ideaContent"
        ></textarea>
      </div>
      <hr class="border-gray-200 dark:border-gray-600" />
      <div class="flex justify-end mt-2">
        <div
          :class="{
            'text-gray-400 hover:text-gray-400': ideaContent.trim() == '',
          }"
          class="w-12 h-8 flex items-center justify-center border rounded-lg bg-white dark:bg-black border-gray-300 dark:border-gray-600 cursor-pointer"
          @click="publishIdea"
        >
          发布
        </div>
      </div>
    </div>
    <!-- 划线详情 -->
    <div
      v-if="showHilightDetail"
      class="absolute top-1/2 left-1/2 -translate-x-1/2 w-160 border border-gray-300 dark:border-gray-600 bg-white dark:bg-black rounded-2xl p-4 z-10"
    >
      <div class="flex justify-start items-center">
        <button
          class="rounded-full border border-gray-300 dark:border-gray-600 w-6 h-6 flex items-center justify-center p-1"
          @click="showHilightDetail = false"
        >
          <CloseIcon
            :size="12"
            :theme="themeStore.themeLight ? 'light' : 'dark'"
          />
        </button>
        <button
          v-show="selectedIdea"
          class="flex items-center border border-gray-300 dark:border-gray-600 rounded-lg px-2 py-1 ml-2 text-sm"
        >
          <IdeaIcon
            :size="16"
            :theme="themeStore.themeLight ? 'light' : 'dark'"
          />
          <span>查看想法</span>
        </button>
      </div>
      <div class="flex ml-4 mt-4 items-start">
        <span class="text-md text-[#6e6d6d] dark:text-[#eaeaea]">{{
          highlightDetail.lineContent
        }}</span>
        <QuoteIcon class="-translate-y-2 ml-2" />
      </div>
      <div class="flex justify-between mt-8 mr-2">
        <div
          class="flex items-center justify-start ml-4 border-t-2 border-gray-300 h-12 w-64"
        >
          <span class="text-md text-[#6e6d6d] dark:text-[#eaeaea] mr-4"
            >划线颜色</span
          >
          <li
            @click="changeHighlightColor('yellow', highlightDetail)"
            class="w-6 h-6 mx-1 rounded-full bg-yellow-300"
            :class="{
              'border-2 border-[#4a4949] dark:border-[#eaeaea]':
                selectedHighlight == 'yellow',
            }"
          ></li>
          <li
            @click="changeHighlightColor('green', highlightDetail)"
            class="w-6 h-6 mx-1 rounded-full bg-[#84c984]"
            :class="{
              'border-2 border-[#4a4949] dark:border-[#eaeaea]':
                selectedHighlight == 'green',
            }"
          ></li>
          <li
            @click="changeHighlightColor('red', highlightDetail)"
            class="w-6 h-6 mx-1 rounded-full bg-red-500"
            :class="{
              'border-2 border-[#4a4949] dark:border-[#eaeaea]':
                selectedHighlight == 'red',
            }"
          ></li>
          <li
            @click="changeHighlightColor('skyblue', highlightDetail)"
            class="w-6 h-6 mx-1 rounded-full bg-[#58c4f3]"
            :class="{
              'border-2 border-[#4a4949] dark:border-[#eaeaea]':
                selectedHighlight == 'skyblue',
            }"
          ></li>
          <li
            @click="changeHighlightColor('purple', highlightDetail)"
            class="w-6 h-6 mx-1 rounded-full bg-purple-500"
            :class="{ 'border-2 border-[#4a4949]': highlightColor == 'purple' }"
          ></li>
        </div>
        <div class="flex justify-end">
          <button
            @click="removeHighlight(highlightDetail)"
            class="mx-2 w-12 h-8 flex items-center justify-center border rounded-lg bg-white dark:bg-black border-gray-300 dark:border-gray-600 cursor-pointer"
          >
            删除
          </button>
          <button
            @click="copyHighlight(highlightDetail.lineContent)"
            class="mx-2 w-12 h-8 flex items-center justify-center border rounded-lg bg-white dark:bg-black border-gray-300 dark:border-gray-600 cursor-pointer"
          >
            复制
          </button>
        </div>
      </div>
    </div>
    <div
      class="fixed top-0 left-0 right-0 bottom-0 opacity-0 pointer-events-none"
    ></div>
  </div>
</template>

<script setup>
import CloseIcon from "@/components/icons/CloseIcon.vue";
import IdeaIcon from "@/components/icons/IdeaIcon.vue";
import QuoteIcon from "@/components/icons/QuoteIcon.vue";
import Epub, { Book, EpubCFI, Layout, Rendition } from "epubjs";
import Themes from "epubjs/src/themes";
import { ref, onUnmounted, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useThemeStore } from "@/stores/theme-store.js";
import { useUtilStore } from "@/stores/util-store.js";
import { useUserStore } from "@/stores/user.js";
import { useBookStore } from "@/stores/book-store.js";
import { useInitStore } from "@/stores/init-store.js";
import {
  uploadUserAnnotation,
  getUserAnnotations,
  deleteUserAnnotation,
  updateUserAnnotation,
  getPublicIdeaPos,
} from "@/api/user-api.js";
import { getFonts } from "@/api/book-api.js";
import { getReadingRecord } from "@/api/book-api.js";
import { ReadingStatus } from "@/utils/enums.js";
const emits = defineEmits(["popup", "hidePop", "openIdea"]);

const props = defineProps({
  openSetting: {
    type: Number,
    required: true,
  },
  bookInformation: {
    type: Object,
    required: false,
  },
});

const showDot = ref(2);
const route = useRoute();
const book = ref(null);
const rendition = ref(null);
const tocList = ref([]);
const locations = ref(null);
const fonts = ref([]);
const screenSize = ref("spread");
const readingRecord = ref(null);
const finishedPage = ref(false);
const readFinished = ref(false);
const themeStore = useThemeStore();
const utilStore = useUtilStore();
const userStore = useUserStore();
const bookStore = useBookStore();
const highLightTocIndex = ref(null);
const currentSelectionText = ref(null);
const currentCFIRange = ref(null);
const isPopupShow = ref(false);
const annotations = ref([]);
const showIdeaEditor = ref(false);
const ideaContent = ref("");
const selectedIdea = ref(null);
const showHilightDetail = ref(false);
const showIdeaDetail = ref(false);
const highlightDetail = ref(null);
const highlightColor = ref("yellow");
const selectedHighlight = ref("yellow");
const initStore = useInitStore();
const publicIdeaPos = ref([]);

onMounted(() => {
  refreshAnnotations();
  getPublicIdeaPos(route.params.id).then((result) => {
    publicIdeaPos.value = result;
  });
  highlightColor.value = initStore.highlightColor;
  getReadingRecord(route.params.id).then((result) => {
    readingRecord.value = result;
    if (result.readingStatus == ReadingStatus.READOVER) {
      readFinished.value = true;
    }
  });
});

// 切换划线颜色
const changeHighlightColor = (color, item) => {
  highlightColor.value = color;
  item.lineColor = color;
  initStore.setHighlightColor(color);
  updateUserAnnotation(item).then(() => {
    rendition.value.annotations.remove(item.epubCfiRange, "highlight");
    rendition.value.annotations.highlight(
      item.epubCfiRange,
      {},
      (e) => {
        openHilightDetail(e.target.attributes["data-epubcfi"].value);
      },
      "epubjs-hl",
      {
        fill: color,
        style: "cursor: pointer;",
      }
    );
  });
};

// 删除划线
const removeHighlight = (item) => {
  console.log("remove highlight: ", item);
  deleteUserAnnotation(item.id).then(() => {
    rendition.value.annotations.remove(item.epubCfiRange, "highlight");
    showHilightDetail.value = false;
  });
};

const copyHighlight = (content) => {
  navigator.clipboard.writeText(content).then(() => {
    utilStore.showMessage("已复制到剪贴板");
    showHilightDetail.value = false;
  });
};

// 发布想法
const publishIdea = () => {
  console.log("publish idea: ", ideaContent.value);
  if (ideaContent.value.trim() == "") {
    return;
  }
  rendition.value.annotations.underline(currentCFIRange.value, {}, (e) => {
    openIdea(e.target.attributes["data-epubcfi"].value);
  });

  const location = getCurrentLocation();
  // 上传
  uploadUserAnnotation({
    userId: userStore.userInfo.userId,
    bookId: bookStore.bookInfo.id,
    cfiRange: currentCFIRange.value,
    type: "idea",
    lineContent: currentSelectionText.value,
    ideaContent: ideaContent.value,
    tocParentIndex: location.mapTocIndex[0],
    tocChildIndex: location.mapTocIndex[1],
  }).then(() => {
    utilStore.showMessage("想法已发布");
    showIdeaEditor.value = false;
    ideaContent.value = "";
    refreshAnnotations();
  });
};

// 发起请求获取书本标注
const refreshAnnotations = () => {
  getUserAnnotations(route.params.id).then((result) => {
    annotations.value = result;
  });
};

// 获取目录标题
const getTocTitle = (tocParentIndex, tocChildIndex) => {
  if (tocChildIndex != null) {
    return tocList.value[tocParentIndex].subitems[tocChildIndex].label;
  } else {
    return tocList.value[tocParentIndex].label;
  }
};

// 切换字体大小
const changeFontSize = (i) => {
  showDot.value = i;
  rendition.value.themes.fontSize(12 + i * 2 + "px");
};

// 切换字体
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
const displayToHref = (href) => {
  rendition.value.display(href).then(() => {
    const location = rendition.value.currentLocation();
    setHighLight(location);
  });
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

// 标记读完
const markFinished = () => {
  request.markFinished(route.params.id).then((result) => {
    readFinished.value = true;
    router.push({ path: "/bookshelf" });
  });
};

const openIdea = (cfi) => {
  console.log("open idea editor: ", cfi);
  emits("openIdea", { bookId: route.params.id, cfi: cfi });
  // annotations.value.forEach((annotation) => {
  //   annotation.userAnnotationList.forEach((item) => {
  //     if (item.type === "idea" && item.epubCfiRange === cfi) {
  //       selectedIdea.value = item;
  //       return;
  //     }
  //   });
  // });
};

const openHilightDetail = (cfi) => {
  annotations.value.forEach((annotation) => {
    annotation.userAnnotationList.forEach((item) => {
      if (item.type === "highlight" && item.epubCfiRange === cfi) {
        selectedHighlight.value = item.lineColor;
        highlightDetail.value = item;
        showHilightDetail.value = true;
      } else if (item.type === "idea" && item.epubCfiRange === cfi) {
        showIdeaDetail.value = true;
      }
    });
  });
};

const setHighLight = (location) => {
  // console.log(location);
  if (screenSize.value == "scroll") {
    return;
  }

  if (highLightTocIndex.value != null) {
    if (highLightTocIndex.value[1] == null) {
      tocList.value[highLightTocIndex.value[0]].highLight = false;
    } else {
      tocList.value[highLightTocIndex.value[0]].subitems[
        highLightTocIndex.value[1]
      ].highLight = false;
    }
  }
  highLightTocIndex.value = location.mapTocIndex;
  if (location.mapTocIndex[1] != null) {
    tocList.value[location.mapTocIndex[0]].subitems[
      location.mapTocIndex[1]
    ].highLight = true;
  } else {
    tocList.value[location.mapTocIndex[0]].highLight = true;
  }
};

// 渲染Epub
const renderEpub = (url) => {
  // 设置布局模式
  let layout = new Layout({
    layout: "flow", // 默认为可重排文本
    minSpreadWidth: 800, // 当宽度超过800px时使用对页布局
    evenSpreads: false, // 不强制偶数对页
  });

  // 根据屏幕宽度切换对页显示
  let screenWidth = window.innerWidth;
  if (screenWidth >= 800) {
    // 使用对页布局
    screenSize.value = "spread";
    let spreadResult = layout.spread("auto", 800);
    console.log("Using spread layout:", spreadResult);
  } else {
    // 使用单页布局
    screenSize.value = "single";
    let spreadResult = layout.spread("none", 0);
    console.log("Using single page layout:", spreadResult);
  }

  // 加载书本
  book.value = new Book(url);
  book.value.loaded.navigation.then((doc) => {
    tocList.value = doc.toc;
    book.value.locations.generate();
    locations.value = book.value.locations;
  });
  // 渲染
  console.log("screenWidth: ", screenWidth);
  if (screenWidth >= 640) {
    rendition.value = book.value.renderTo("read", {
      width: "93%",
      height: "90%",
      allowScriptedContent: true,
      layout: layout,
      spread: "auto",
      manager: "continuous",
    });
  } else {
    screenSize.value = "scroll";
    rendition.value = book.value.renderTo("read", {
      width: "100%",
      height: "100%",
      allowScriptedContent: true,
      manager: "continuous",
      flow: "scrolled",
    });
  }

  // 获取字体列表
  getFonts().then((res) => {
    fonts.value = res;
    // 注册字体CSS
    fonts.value.forEach((font) => {
      if (font.url != null) {
        rendition.value.hooks.content.register((contents) => {
          contents.addStylesheetRules(
            {
              "@font-face": {
                "font-family": font.fontValue,
                src: `url(${font.url})`,
              },
            },
            "fonts"
          );
        });
      }
    });
  });

  // 设置默认样式
  let themes = new Themes(rendition.value);

  // 展示
  rendition.value.display().then(() => {
    console.log(readingRecord.value);
    // 定位上次阅读位置
    if (
      readingRecord.value != null &&
      readingRecord.value.lastReadCfi != null
    ) {
      // 展示上次阅读的进度
      rendition.value.display(readingRecord.value.lastReadCfi).then(() => {
        // 定位当前目录位置并添加高亮
        const location = rendition.value.currentLocation();
        setHighLight(location);
      });
    }

    // 渲染用户划线
    annotations.value.forEach((annotation) => {
      annotation.userAnnotationList.forEach((item) => {
        if (item.type === "highlight") {
          rendition.value.annotations.highlight(
            item.epubCfiRange,
            {},
            (e) => {
              openHilightDetail(e.target.attributes["data-epubcfi"].value);
            },
            "epubjs-hl",
            {
              fill: item.lineColor,
              style: "cursor: pointer;",
            }
          );
        } else if (item.type === "idea") {
          // rendition.value.annotations.underline(
          //   item.epubCfiRange,
          //   {},
          //   (e) => {
          //     // console.log(e.target.attributes["data-epubcfi"].value);
          //     openIdea(e.target.attributes["data-epubcfi"].value);
          //   },
          //   "epubjs-hl",
          //   {
          //     stroke: "red",
          //     style: "cursor: pointer;",
          //   }
          // );
        }
      });
    });

    publicIdeaPos.value.forEach((idea) => {
      rendition.value.annotations.underline(
        idea.epubCfiRange,
        {},
        (e) => {
          // console.log(e.target.attributes["data-epubcfi"].value);
          openIdea(e.target.attributes["data-epubcfi"].value);
        },
        "epubjs-hl",
        {
          stroke: "red",
          style: "cursor: pointer;",
        }
      );
    });

    // 设置默认主题样式
    themes.default({
      p: {
        margin: "0.5em 1em !important",
      },
      ".block_9,.block_12": {
        "line-height": "1.8",
        margin: "12px",
        "text-indent": "0",
      },
      ".calibre": {
        "font-size": "1.1em",
      },
      ".calibre8, .x4, .x9": {
        "font-family": "inherit !important",
      },
      ".theme-dark p": {
        color: "#fff",
      },
    });

    // 注册亮色主题
    themes.registerThemes({
      "theme-dark": {
        p: {
          color: "#fff",
        },
        color: "#fff",
      },
    });

    // 注册暗色主题
    themes.registerThemes({
      "theme-light": {
        p: {
          color: "#000",
        },
        color: "#000",
      },
    });

    // 切换主题
    if (themeStore.themeLight) {
      rendition.value.themes.select("theme-light");
    } else {
      rendition.value.themes.select("theme-dark");
    }
  });

  // 文字选中后显示弹窗
  rendition.value.hooks.content.register((contents) => {
    // 监听鼠标抬起事件
    contents.window.addEventListener("mouseup", (e) => {
      const framePos = contents.window.frameElement.getBoundingClientRect();
      // console.log("framePos: ", framePos);
      // isPopupShow.value = true;
      setTimeout(() => {
        // console.log("mouse up: ", e.clientX, e.clientY);

        const selection = contents.window.getSelection();
        const range = selection.getRangeAt(0);
        console.log("selection range: ", range);
        const selectedText = selection.toString().trim();
        if (selection && selectedText.length > 0 && selection.rangeCount > 0) {
          currentSelectionText.value = selectedText;
          emits("popup", {
            x: e.clientX + framePos.left,
            y: e.clientY + framePos.top + 10,
          });
          // popup.style.left = e.clientX + framePos.left + "px";
          // popup.style.top = e.clientY + framePos.top + 10 + "px";
          // popup.classList.add("show");
          const range = selection.getRangeAt(0);
          if (!range.collapsed) {
            const cfirange = new EpubCFI(range, contents.cfiBase).toString();
            currentCFIRange.value = cfirange;
            console.log(cfirange);
          }
        } else {
          emits("hidePop");
        }
      }, 10);
    });
  });
};

const refreshBookTheme = () => {
  if (themeStore.themeLight) {
    rendition.value.themes.select("theme-light");
  } else {
    rendition.value.themes.select("theme-dark");
  }
};

const onHighlightClick = (e) => {
  console.log(e.clientX, e.clientY);
  showPopup(e.clientX, e.clientY);
};

const showPopup = (x, y) => {
  popup.value.style.left = x + "px";
  popup.value.style.top = y + 10 + "px";
  popup.value.classList.add("show");
  console.log(popup.value);
};

const hidePopup = () => {
  popup.value.classList.remove("show");
};

const destroyEpub = () => {
  if (rendition.value) {
    rendition.value.destroy();
  }
  if (book.value) {
    book.value = null;
  }
};

const getCurrentLocation = () => {
  if (rendition.value) {
    return rendition.value.currentLocation();
  }
  return null;
};

const getProgress = () => {
  if (locations.value && rendition.value) {
    const location = rendition.value.currentLocation();
    return locations.value.percentageFromCfi(location.start.cfi);
  }
  return 0;
};

document.addEventListener("mouseup", (e) => {
  console.log("window mouse up:", e.clientX, e.clientY);
});

const handleCopy = () => {
  if (!currentSelectionText.value) {
    return;
  }

  if (navigator.clipboard && navigator.clipboard.writeText) {
    navigator.clipboard.writeText(currentSelectionText.value).then(() => {
      utilStore.showMessage("已复制到剪贴板");
    });
  }

  emits("hidePop");
};

const handleHighlight = () => {
  if (currentCFIRange.value) {
    rendition.value.annotations.highlight(
      currentCFIRange.value,
      {},
      (e) => {
        openHilightDetail(e.target.attributes["data-epubcfi"].value);
      },
      "epubjs-hl",
      {
        fill: highlightColor.value,
      }
    );
    const location = getCurrentLocation();

    // 上传
    uploadUserAnnotation({
      userId: userStore.userInfo.userId,
      bookId: bookStore.bookInfo.id,
      cfiRange: currentCFIRange.value,
      type: "highlight",
      lineColor: highlightColor.value,
      lineContent: currentSelectionText.value,
      tocParentIndex: location.mapTocIndex[0],
      tocChildIndex: location.mapTocIndex[1],
    }).then(() => {
      refreshAnnotations();
    });
  }
};

const handleHighlightIdea = (cfi, text) => {
  console.log(cfi, text);
  rendition.value.annotations.highlight(
    cfi,
    {},
    (e) => {
      openHilightDetail(e.target.attributes["data-epubcfi"].value);
    },
    "epubjs-hl",
    {
      fill: highlightColor.value,
    }
  );
  const location = getCurrentLocation();

  // 上传
  uploadUserAnnotation({
    userId: userStore.userInfo.userId,
    bookId: bookStore.bookInfo.id,
    cfiRange: cfi,
    type: "highlight",
    lineColor: highlightColor.value,
    lineContent: text,
    tocParentIndex: location.mapTocIndex[0],
    tocChildIndex: location.mapTocIndex[1],
  }).then(() => {
    refreshAnnotations();
  });
};

const handleIdeaEdit = (data) => {
  if (data) {
    currentCFIRange.value = data.epubCfiRange;
    currentSelectionText.value = data.lineContent;
  }
  if (currentCFIRange.value) {
    showIdeaEditor.value = true;
    emits("hidePop");
  }
};

defineExpose({
  renderEpub,
  destroyEpub,
  getCurrentLocation,
  getProgress,
  refreshBookTheme,
  handleCopy,
  handleHighlight,
  handleIdeaEdit,
  handleHighlightIdea,
  handleIdeaEdit,
});
</script>

<style>
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
