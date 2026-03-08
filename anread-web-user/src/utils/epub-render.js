import Epub, { Book, EpubCFI, Layout, Rendition } from "epubjs";
import Themes from "epubjs/src/themes";
import { ref } from "vue";
import { useThemeStore } from "@/stores/theme-store.js";
import { useUtilStore } from "@/stores/util-store.js";
import { useUserStore } from "@/stores/user.js";
import { useBookStore } from "@/stores/book-store.js";
import request from "@/utils/request.js";
import { uploadUserAnnotation, getUserAnnotations } from "@/api/user-api.js";

const book = ref(null);
const rendition = ref(null);
const tocList = ref([]);
const locations = ref(null);
const fonts = ref([]);
const screenSize = ref("spread");
const readingRecord = ref(null);
const themeStore = useThemeStore();
const utilStore = useUtilStore();
const userStore = useUserStore();
const bookStore = useBookStore();
const highLightTocIndex = ref(null);
const currentSelectionText = ref(null);
const currentCFIRange = ref(null);

const popupHTML = `
      <div class="popup-item" id="copyBtn">
        <svg
          class="icon"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
          <path
            d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"
          ></path>
        </svg>
        复制
      </div>
      <div class="popup-item" id="highlightBtn">
        <svg
          class="icon"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <path d="M9 11l3 3L22 4"></path>
          <path
            d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"
          ></path>
        </svg>
        高亮
      </div>
`;

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
      height: "96%",
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

  // 设置默认样式
  let themes = new Themes(rendition.value);

  // 展示
  rendition.value.display().then(() => {
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
    getUserAnnotations(bookStore.bookInfo.id).then((data) => {
      console.log("user annotations: ", data);
      if (data) {
        data.forEach((annotation) => {
          if (annotation.type === "highlight") {
            rendition.value.annotations.highlight(annotation.epubCfiRange);
          }
        });
      }
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
    // 注册弹窗样式
    contents.addStylesheet("/css/popup.css");
    // 创建弹窗组件
    const contentsBody = contents.document.getElementsByTagName("body")[0];
    const popup = contents.document.createElement("div");
    popup.setAttribute("id", "popup");
    popup.innerHTML = popupHTML;
    contentsBody.appendChild(popup);

    // 复制功能
    const copyBtn = contents.document.getElementById("copyBtn");
    copyBtn.addEventListener("click", function () {
      if (!currentSelectionText.value) {
        return;
      }

      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(currentSelectionText.value).then(() => {
          utilStore.showMessage("已复制到剪贴板");
        });
      }

      popup.classList.remove("show");
    });

    // 高亮功能
    const highlightBtn = contents.document.getElementById("highlightBtn");
    highlightBtn.addEventListener("click", () => {
      if (currentCFIRange.value) {
        rendition.value.annotations.highlight(currentCFIRange.value);
        // 上传
        uploadUserAnnotation({
          userId: userStore.userInfo.userId,
          bookId: bookStore.bookInfo.id,
          cfiRange: currentCFIRange.value,
          type: "highlight",
          lineColor: "yellow",
          lineContent: currentSelectionText.value,
        });
      }
    });

    // 监听鼠标抬起事件
    contents.window.addEventListener("mouseup", (e) => {
      setTimeout(() => {
        console.log("mouse up: ", e.clientX, e.clientY);

        const selection = contents.window.getSelection();
        const selectedText = selection.toString().trim();
        currentSelectionText.value = selectedText;
        if (selection && selectedText.length > 0 && selection.rangeCount > 0) {
          popup.style.left = e.clientX + "px";
          popup.style.top = e.clientY + 10 + "px";
          popup.classList.add("show");
          const range = selection.getRangeAt(0);
          if (!range.collapsed) {
            const cfirange = new EpubCFI(range, contents.cfiBase).toString();
            currentCFIRange.value = cfirange;
            console.log(cfirange);
          }
        } else {
          popup.classList.remove("show");
        }
      }, 10);
    });
  });
};

export {
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
};
