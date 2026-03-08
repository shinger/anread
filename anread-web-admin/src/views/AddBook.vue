<template>
  <div class="add">
    <el-card class="add-container">
      <el-form
        :model="state.bookForm"
        :rules="state.rules"
        ref="bookRef"
        label-width="100px"
        class="bookForm"
      >
        <el-form-item required label="分类">
          <el-cascader
            :placeholder="state.defaultCate"
            style="width: 300px"
            :props="state.category"
            @change="handleChangeCate"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="书籍名称" prop="bookName">
          <el-input
            style="width: 300px"
            v-model="state.bookForm.bookName"
            placeholder="请输入书籍名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input
            style="width: 300px"
            v-model="state.bookForm.author"
            placeholder="请输入作者姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="bookIntro">
          <el-input
            style="width: 600px"
            type="textarea"
            v-model="state.bookForm.bookIntro"
            placeholder="请输入书本简介"
          ></el-input>
        </el-form-item>
        <el-form-item label="出版社" prop="press">
          <el-input
            style="width: 300px"
            v-model="state.bookForm.press"
            placeholder="请输入出版社全称"
          ></el-input>
        </el-form-item>
        <el-form-item label="出版年月" prop="press">
          <el-date-picker
            v-model="state.bookForm.pressDate"
            type="month"
            placeholder="Pick one or more months"
            value-format="YYYY-MM"
          />
        </el-form-item>
        <el-form-item label="字数" prop="wordCount">
          <el-input
            type="number"
            min="0"
            style="width: 300px"
            v-model="state.bookForm.wordCount"
            placeholder="请输入字数（万字）"
          ></el-input>
        </el-form-item>
        <el-form-item label="上架状态" prop="bookSellStatus">
          <el-radio-group v-model="state.bookForm.bookStatus">
            <el-radio label="1">上架</el-radio>
            <el-radio label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Epub格式文件" prop="epubFile">
          <el-upload
            class="avatar-uploader"
            accept=".epub"
            :show-file-list="false"
            :before-upload="handleBeforeUpload"
            :on-success="handleUrlSuccess"
          >
            <img
              style="width: 120px; height: 180px; border: 1px solid #e9e9e9"
              v-if="state.bookForm.coverImg"
              :src="state.bookForm.coverImg"
              class="avatar"
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <!-- <el-form-item label="详情内容">
          <div ref='editor'></div>
        </el-form-item> -->
        <el-form-item style="margin-top: 92px">
          <el-button type="primary" @click="submitAdd()">{{
            state.id ? "立即修改" : "立即创建"
          }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {
  reactive,
  ref,
  onMounted,
  onBeforeUnmount,
  getCurrentInstance,
} from "vue";
// import WangEditor from 'wangeditor'
import adminAPI from "@/requests/admin-api.js";
import fileAPI from "@/requests/file-api.js";
import bookAPI from "@/requests/book-api.js";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import { localGet, uploadBookServer, uploadImgsServer } from "@/utils";

const { proxy } = getCurrentInstance();
const editor = ref(null);
const bookRef = ref(null);
const route = useRoute();
const router = useRouter();
const { id } = route.query;

const handleChangePressDate = (val) => {
  console.log(state.bookForm);
}

const state = reactive({
  uploadBookServer: "/uploadbook",
  token: localGet("token") || "",
  id: id,
  defaultCate: "",
  bookForm: {
    bookName: "",
    author: "",
    bookIntro: "",
    press: "",
    pressDate: "",
    wordCount: "",
    bookStatus: "0",
    epubFile: "",
    coverImg: "",
    fileId: "",
  },
  rules: {
    bookName: [
      { required: "true", message: "请填写书本名称", trigger: ["change"] },
    ],
    author: [
      { required: "true", message: "请填写书本作者", trigger: ["change"] },
    ],
    bookIntro: [
      { required: "true", message: "请填写书本简介", trigger: ["change"] },
    ],
    press: [{ required: "true", message: "请填写出版社", trigger: ["change"] }],
    wordCount: [
      { required: "true", message: "请填写书本字数", trigger: ["change"] },
    ],
  },
  categoryId: "",
  category: {
    lazy: true,
    lazyLoad(node, resolve) {
      const { value } = node;
      // 获取分类列表
      adminAPI
        .requestCategories({
          parentId: value || 0,
        })
        .then((res) => {
          const list = res;
          const nodes = list.map((item) => ({
            value: item.id,
            label: item.categoryName,
            parentId: item.parentId,
            leaf: item.parentId > 0,
          }));
          resolve(nodes);
        });
    },
  },
});

onMounted(() => {
  if (id) { // 编辑书本时，获取书本详情
    adminAPI.requestBookDetail(id).then((res) => {
      console.log(res);
      if (res.pressMonth < 10) {
        res.pressMonth = "0" + res.pressMonth;
      }
      state.bookForm = {
        bookName: res.title,
        author: res.author,
        bookIntro: res.introduction,
        press: res.press,
        pressDate: res.pressYear + "-" + res.pressMonth,
        wordCount: res.wordCount,
        bookStatus: res.status,
        epubFile: "",
        coverImg: res.cover,
        fileId: res.fileId,
        bookStatus: res.status + "",
      };
      state.categoryId = res.categoryId;
      state.defaultCate = `${res.mainCategory}/${res.subCategory}`;
      console.log(state)
    });
  }
});

// 上传 epub 文件
const uploadbook = (file) => {
  console.log(file);
  const formData = new FormData();
  formData.append("file", file);
  console.log(formData);

  fileAPI
    .uploadBookFile(formData)
    .then((response) => {
      console.log("上传成功", response.data);
      state.bookForm.coverImg = response.coverImg;
      state.bookForm.fileId = response.id;
    })
    .catch((error) => {
      console.error("上传失败", error);
    });
};

// 提交添加或修改书本
const submitAdd = () => {
  bookRef.value.validate((vaild) => {
    if (vaild) {
      console.log(state);
      let params = {
        categoryId: state.categoryId,
        title: state.bookForm.bookName,
        author: state.bookForm.author,
        introduction: state.bookForm.bookIntro,
        press: state.bookForm.press,
        pressDate: state.bookForm.pressDate,
        wordCount: state.bookForm.wordCount,
        coverImg: state.bookForm.coverImg,
        fileId: state.bookForm.fileId,
        status: state.bookForm.bookStatus,
      };
      console.log("params", params);
      if (id) {
        params.bookId = id;
        // 修改书本使用 put 方法
        adminAPI.updateBook(params).then(() => {
          ElMessage.success("修改成功");
          router.push({ path: "/book" });
        });
        return;
      }
      adminAPI.uploadBook(params).then(() => {
        ElMessage.success("添加成功");
        router.push({ path: "/book" });
      });
    }
  });
};
// 文件格式校验
const handleBeforeUpload = (file) => {
  const sufix = file.name.split(".")[1] || "";
  if (!["epub"].includes(sufix)) {
    ElMessage.error("请上传 epub 格式的文件");
    return false;
  }
  uploadbook(file);
};
// 回显封面图片
const handleUrlSuccess = (val) => {
  if (val.code != "201") {
    ElMessage.error("文件上传失败");
  }
  console.log(val);
  state.bookForm.coverImg = val.result.coverImg || "";
  state.bookForm.fileId = val.result.id || "";
};
// 分类选择器改变事件
const handleChangeCate = (val) => {
  console.log(val);
  // console.log(state.category)
  state.categoryId = val[1] || 0;
};
</script>

<style scoped>
.add {
  display: flex;
}
.add-container {
  flex: 1;
  height: 100%;
}
.avatar-uploader {
  width: 100px;
  height: 100px;
  color: #ddd;
  font-size: 30px;
}
.avatar-uploader-icon {
  display: block;
  width: 100%;
  height: 100%;
  border: 1px solid #e9e9e9;
  padding: 32px 17px;
}
</style>
