<template>
  <main>
    <div class="flex justify-center items-center">
      <div
        @click="selectFile"
        class="w-68 h-48 mt-30 sm:w-lg sm:h-64 rounded-md sm:rounded-lg flex flex-col items-center justify-center border-dashed border-2 border-gray-300 cursor-pointer"
      >
        <h1 class="text-xl font-bold">点击上传图书</h1>
        <p class="text-sm text-gray-500">
          支持上传EPUB/PDF文件，上传完成后可在「书架」中查看
        </p>
        <input
          class="hidden"
          type="file"
          ref="fileInput"
          @change="handleFileChange"
          :disabled="uploadState === 'loading'"
          accept=".epub,.pdf"
        />
        <Loading v-if="uploadState === 'loading'" message="上传中..." />
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref } from "vue";
import { uploadPrivateBook, convertPdfToEpub } from "@/api/book-api.js";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user.js";
import Loading from "@/components/Loading.vue";

const router = useRouter();
const file = ref(null);
const fileInput = ref();
const userStore = useUserStore();
const userId = userStore.getUserId();
const uploadState = ref("none");

const handleFileChange = (event) => {
  const selectedFile = event.target.files[0];
  file.value = selectedFile;
  uploadFile();
};

const selectFile = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

const uploadFile = async () => {
  if (!file.value) {
    return;
  }

  const formData = new FormData();
  formData.append("file", file.value);
  uploadState.value = "loading";
  if (file.value.type === "application/pdf") {
    convertPdfToEpub(formData, userId)
      .then((response) => {
        uploadState.value = "success";
        router.push("/bookshelf");
      })
      .catch((error) => {
        console.error("转换失败", error);
      });
    return;
  }

  uploadPrivateBook(formData)
    .then((response) => {
      uploadState.value = "success";
      router.push("/bookshelf");
    })
    .catch((error) => {
      console.error("上传失败", error);
    });
};
</script>

<style>
</style>