<template>
  <el-dialog
    :title="state.type == 'add' ? '添加分类' : '修改分类'"
    v-model="state.visible"
    width="400px"
  >
    <el-form
      :model="state.ruleForm"
      :rules="state.rules"
      ref="formRef"
      label-width="100px"
      class="good-form"
    >
      <el-form-item label="分类名" prop="name">
        <el-input type="text" v-model="state.ruleForm.name"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="state.visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRoute } from "vue-router";
import adminAPI from "@/requests/admin-api.js";
import { ElMessage } from "element-plus";

const props = defineProps({
  type: String, // 用于判断是添加还是编辑
  reload: Function, // 添加或修改完后，刷新列表页
});

const formRef = ref(null);
const route = useRoute();
const state = reactive({
  visible: false,
  parentId: 0,
  ruleForm: {
    name: "",
  },
  rules: {
    name: [{ required: "true", message: "名称不能为空", trigger: ["change"] }],
    // subCategory: [
    //   { required: 'true', message: '编号不能为空', trigger: ['change'] }
    // ]
  },
  id: "",
});
// 获取详情
const getDetail = (id) => {
  adminAPI.requestCategoryDetail(id).then((res) => {
    state.ruleForm = {
      name: res.categoryName,
      rank: res.categoryRank,
    };
    state.parentId = res.parentId;
    state.categoryLevel = res.categoryLevel;
  });
};
// 开启弹窗
const open = (id) => {
  state.visible = true;
  if (id) {
    state.id = id;
    // 如果是有 id 传入，证明是修改模式
    getDetail(id);
  } else {
    // 否则为新增模式
    // 新增类目，从路由获取分类 level 级别和父分类 id
    const { level = 1, parent_id = 0 } = route.query;
    state.ruleForm = {
      name: "",
      rank: "",
    };
    state.parentId = parent_id;
    state.categoryLevel = level;
  }
};
// 关闭弹窗
const close = () => {
  state.visible = false;
};
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (props.type == "add") {
        // 添加方法
        adminAPI
          .addCategory({
            parentId: state.parentId,
            categoryName: state.ruleForm.name,
          })
          .then(() => {
            ElMessage.success("添加成功");
            state.visible = false;
            // 接口回调之后，运行重新获取列表方法 reload
            if (props.reload) props.reload();
          });
      } else {
        // 修改方法
        adminAPI
          .updateCategory({
            id: state.id,
            categoryName: state.ruleForm.name,
          })
          .then(() => {
            ElMessage.success("修改成功");
            state.visible = false;
            // 接口回调之后，运行重新获取列表方法 reload
            if (props.reload) props.reload();
          });
      }
    }
  });
};
defineExpose({ open, close });
</script>
