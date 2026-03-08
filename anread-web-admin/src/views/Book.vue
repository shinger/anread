<template>
  <el-card class="book-container">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="handleAdd"
          >新增书籍</el-button
        >
      </div>
    </template>
    <el-table
      :load="state.loading"
      :data="state.tableData"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID"> </el-table-column>
      <el-table-column prop="title" label="书名"> </el-table-column>
      <el-table-column prop="author" label="作者"> </el-table-column>
      <el-table-column prop="press" label="出版社"> </el-table-column>
      <el-table-column prop="pressDate" label="出版时间">
        <template #default="scope">
          {{ scope.row.pressYear }}-{{ scope.row.pressMonth }}
        </template>
      </el-table-column>
      <el-table-column label="分类">
        <template #default="scope">
          {{ scope.row.mainCategory }}-{{ scope.row.subCategory }}
        </template>
      </el-table-column>
      <el-table-column prop="introduction" label="简介" show-overflow-tooltip>
      </el-table-column>
      <el-table-column label="封面" width="150px">
        <template #default="scope">
          <img
            style="width: 100px; height: 100px"
            :key="scope.row.title"
            :src="scope.row.cover"
            alt="封面"
          />
        </template>
      </el-table-column>
      <el-table-column prop="readership" label="阅读人数"> </el-table-column>
      <el-table-column prop="wordCount" label="字数"> </el-table-column>
      <el-table-column label="上架状态">
        <template #default="scope">
          <span style="color: green" v-if="scope.row.status == 1">已上架</span>
          <span style="color: red" v-else>已下架</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="150">
        <template #default="scope">
          <a
            style="cursor: pointer; margin-right: 10px"
            @click="handleEdit(scope.row.id)"
            >修改</a
          >
          <a
            style="cursor: pointer; margin-right: 10px"
            v-if="scope.row.status == 1"
            @click="handleStatus(scope.row.id, 0)"
            >下架</a
          >
          <a
            style="cursor: pointer; margin-right: 10px"
            v-else
            @click="handleStatus(scope.row.id, 1)"
            >上架</a
          >
          <a
            style="cursor: pointer; margin-right: 10px"
            @click="handleDelete(scope.row.id)"
            >删除</a
          >
        </template>
      </el-table-column>
    </el-table>
    <!--总数超过一页，再展示分页器-->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="state.total"
      :page-size="state.pageSize"
      :current-page="state.currentPage"
      @current-change="changePage"
    />
  </el-card>
</template>

<script setup>
import { onMounted, reactive, getCurrentInstance } from "vue";
import adminAPI from "@/requests/admin-api.js";
import { ElMessage } from "element-plus";
import { Plus, Delete } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

const app = getCurrentInstance();
const { goTop } = app.appContext.config.globalProperties;
const router = useRouter();
const state = reactive({
  loading: false,
  tableData: [], // 数据列表
  total: 0, // 总条数
  currentPage: 1, // 当前页
  pageSize: 10, // 分页大小
});
onMounted(() => {
  getbookList();
});
// 获取书籍列表
const getbookList = () => {
  state.loading = true;
  adminAPI
    .requestBooks({
      params: {
        pageNumber: state.currentPage - 1,
        pageSize: state.pageSize,
      },
    })
    .then((res) => {
      console.log(res);
      state.tableData = res.content;
      state.total = res.totalElements;
      state.currentPage = res.number + 1;
      state.loading = false;
      goTop && goTop();
    })
    .catch((error) => {
      console.log(error);
    });
};
const handleAdd = () => {
  router.push({ path: "/add" });
};
const handleEdit = (id) => {
  router.push({ path: "/add", query: { id } });
};
const changePage = (val) => {
  state.currentPage = val;
  getbookList();
};
const handleStatus = (id, status) => {
  adminAPI.updateBookStatus(id, status).then(() => {
    ElMessage.success("修改成功");
    getbookList();
  });
};
const handleDelete = (id) => {
  adminAPI.deleteBook(id).then(() => {
    ElMessage.success("删除成功");
    getbookList();
  });
};
</script>

<style scoped>
.book-container {
  min-height: 100%;
}
.el-card.is-always-shadow {
  min-height: 100% !important;
}
</style>
