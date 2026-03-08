<script setup>
import { ref } from "vue";
import axios from "@/utils/axios.js";

const file = ref(null);
const fileInput = ref();

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

  axios
    .post("/upload/book", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((response) => {
      location.reload()
    })
    .catch((error) => {
      console.error("上传失败", error);
    });
};
</script>

<template>
  <div class="file-upload" @click="selectFile">
    <span>上传本地</span>
    <input
      type="file"
      ref="fileInput"
      @change="handleFileChange"
      accept=".epub"
    />
  </div>
</template>

<style scoped lang="less">
.file-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 2px dashed #d5d1d1;
  width: 128px;
  height: 185px;
  background: url('@/assets/images/plus.png') no-repeat;
  background-size: 48px 48px;
  background-position: 40px 56px;
  &:hover {
    cursor: pointer;
  }
  span {
    margin-top: 120px;
    color: #9b9b9b;
  }
  input {
    display: none;
  }
}
</style>
