<template>
  <div class="flex flex-col justify-evenly items-center">
    <div class="mt-2.5 text-base font-bold">个人主页</div>
    <div class="mt-9 flex flex-col items-center">
      <div class="avatar" @click="selectFile">
        <Avatar size="60" :src="userInfo.avatar" />
        <input
          style="display: none"
          type="file"
          ref="fileInput"
          @change="handleFileChange"
          accept="image/*"
        />
      </div>
      <span class="mt-2.5 text-lg font-semibold text-center leading-4">{{
        userInfo.username
      }}</span>
      <p class="mt-2.5 text-sm text-gray-400">不念过往，不惧将来</p>
    </div>
    <div class="my-5 flex justify-between">
      <div class="mx-8 flex flex-col items-center">
        <div class="text-sm font-semibold">
          {{ readingDuration.hour
          }}<span
            class="text-xs text-gray-400"
            v-show="readingDuration.hour > 0"
            >小时</span
          >
          {{ readingDuration.min }}<span class="text-xs text-gray-400">分</span>
        </div>
        <span class="text-xs text-gray-400">阅读时长</span>
      </div>
      <div class="mx-8 flex flex-col items-center">
        <div class="text-sm font-semibold">
          {{ userInfo.likesCount }}<span class="text-xs text-gray-400">个</span>
        </div>
        <span class="text-xs text-gray-400">收到的赞</span>
      </div>
      <div class="mx-8 flex flex-col items-center">
        <div class="text-sm font-semibold">
          {{ userInfo.followers }}<span class="text-xs text-gray-400">人</span>
        </div>
        <span class="text-xs text-gray-400">关注了我</span>
      </div>
    </div>
    <!-- <div class="w-full flex flex-col items-center rounded-xl dark:bg-black mx-4">
      <div class="mt-3 text-base flex text-gray-300">
        <div class="mx-16" :class="{ active: tabActive == 0 }" @click="tabActive = 0">
          书架·{{ shelfBooks[0].length }}
        </div>
        <div class="mx-16" :class="{ active: tabActive == 1 }" @click="tabActive = 1">
          读完·{{ shelfBooks[1].length }}
        </div>
      </div>
      <div class="flex justify-center self-start border-t border-gray-300 w-full mt-3">
        <a
          :href="`/book/${item.id}`"
          v-show="shelfBooks[tabActive].length > 0"
          class="my-5 mx-4 flex flex-col items-start w-18"
          v-for="item in shelfBooks[tabActive]"
          :key="item.id"
        >
          <img class="w-18 h-24" :src="item.cover" />
          <span class="w-18 text-sm text-gray-300 truncate">{{ item.title }}</span>
        </a>
        <div v-show="shelfBooks[tabActive].length == 0" class="flex justify-center items-center min-h-30 mt-5 text-sm text-gray-300">
          暂无书籍
        </div>
      </div>
      <div class="mb-5"><a href="/bookshelf">查看书架</a></div>
    </div> -->
  </div>
</template>

<script setup>
import Avatar from "@/components/Avatar.vue";
import { ref, reactive, onBeforeMount } from "vue";
import request from "@/utils/request.js";
import { useToast } from "vue-toast-notification";
import { useUserStore } from "@/stores/user";
import { useShelfStore } from "@/stores/shelf";

const toast = useToast();
const file = ref(null);
const fileInput = ref();
const avatarUrl = ref();
const userInfo = ref();
const shelfBooks = reactive([[], []]);
const tabActive = ref(0);
const readingDuration = reactive({
  hour: 0,
  min: 0,
});

const userStore = useUserStore();
const shelfStore = useShelfStore();

const maxSize = 2 * 1024 * 1024;

onBeforeMount(() => {
  // if (shelfStore.shelfBooks !== null) {
  //   request.getShelfBooks().then((response) => {
  //     shelfBooks[0] = response.splice(0, 5);
  //     shelfBooks[1] = shelfBooks[0].filter((item) => item.readingStatus == 2);
  //     shelfStore.setShelfBooks(shelfBooks);
  //   });
  // }

  if (userStore.userInfo != null) {
    userInfo.value = userStore.userInfo;
    readingDuration.hour = Math.floor(userInfo.value.readingDuration / 60);
    readingDuration.min = userInfo.value.readingDuration % 60;
    return;
  }
  request.getUserInfo().then((result) => {
    userInfo.value = result;
    readingDuration.hour = Math.floor(userInfo.value.readingDuration / 60);
    readingDuration.min = userInfo.value.readingDuration % 60;
    userStore.setUserInfo(userInfo.value);
  });
});

const selectFile = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

const handleFileChange = (event) => {
  const selectedFile = event.target.files[0];
  if (selectedFile.size > maxSize) {
    toast.warning("请上传2MB以下的图片格式文件");
  }
  file.value = selectedFile;
  uploadFile();
};

const uploadFile = async () => {
  if (!file.value) {
    return;
  }

  const formData = new FormData();
  formData.append("file", file.value);

  request
    .uploadAvatar(formData)
    .then((response) => {
      userInfo.value.avatar = response;
      location.reload();
    })
    .catch((error) => {
      console.error("上传失败", error);
    });
};
</script>

<style scoped>
.active {
  color: #f39a2c;
}
</style>
