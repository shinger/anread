<template>
  <main>
    <el-card class="font-config-container">
      <el-tabs v-model="activeTab" type="border-card" default-active="font">
        <el-tab-pane label="字体配置" name="font">
          <el-row class="config-bar" :justify="space - between">
            <el-col :span="20"> 字体配置内容 </el-col>
            <el-col :span="4" align="right">
              <el-button @click="uploader = true">添加</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-table :data="fontsData" style="width: 100%">
              <el-table-column prop="fontName" label="字体名称" />
              <el-table-column prop="fontValue" label="字体CSS" />
              <el-table-column prop="fontFileName" label="字体文件">
                <template #default="scope">
                  <a :href="scope.row.url" v-if="scope.row.url">{{
                    scope.row.fontFileName
                  }}</a>
                  <span v-else>{{ scope.row.fontFileName }}</span>
                </template>
              </el-table-column>
              <el-table-column align="right" label="编辑">
                <template #default="scope">
                  <el-button
                    size="small"
                    type="primary"
                    @click="handlePreview(scope.$index, scope.row)"
                  >
                    预览
                  </el-button>
                  <el-button
                    size="small"
                    type="danger"
                    @click="handleDelete(scope.$index, scope.row)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="其他配置" name="other">
          <el-row class="config-bar" justify="space-between">
            <el-col :span="22"> 配置 </el-col>
            <el-col :span="2" align="right"> 编辑 </el-col>
          </el-row>
          <el-row
            class="config-bar"
            style="margin-top: 10px"
            justify="space-between"
            v-for="config in sysConfigs"
            :key="config.id"
          >
            <el-col :span="2" class="config-label">
              {{ config.configLabel }}：
            </el-col>
            <el-col :span="18" class="config-value">
              <p v-show="!modifySysConfig">{{ config.configValue }}</p>
              <el-input
                v-show="modifySysConfig"
                v-model="config.configValue"
                style="width: 240px"
              />
            </el-col>
            <el-row :span="4" align="right">
              <el-button v-show="modifySysConfig" type="success" :icon="Check" circle @click="saveSysConfig(config)" />
              <el-button v-show="modifySysConfig" type="info" :icon="Close" circle @click="modifySysConfig = false" />
              <el-button v-show="!modifySysConfig" type="primary" :icon="Edit" circle @click="modifySysConfig = true" />
            </el-row>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="uploader" title="添加字体" width="500">
      <el-form>
        <el-form-item label="字体名称" prop="fontName">
          <el-input v-model="form.fontName" placeholder="请输入字体名称" />
        </el-form-item>
        <el-form-item label="字体CSS" prop="fontValue">
          <el-input v-model="form.fontValue" placeholder="请输入字体CSS名称" />
        </el-form-item>
        <el-form-item label="字体文件" prop="fontFile">
          <el-upload
            class="avatar-uploader"
            :auto-upload="false"
            :limit="1"
            :on-exceed="handleExceed"
            :on-success="handleFontUploaded"
            :on-change="handleFontChange"
            v-model="form.fontFile"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="uploader = false">关闭</el-button>
          <el-button type="primary" @click="uploadFont"> 上传 </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="previewer" title="字体预览" width="500">
      <div :style="`font-family: ${previewFont}; font-size: 24px;`">
        这是一个预览字体
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="previewer = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </main>
</template>

<script setup>
import { onMounted, reactive, ref, toRefs, onBeforeMount } from "vue";
import bookAPI from "@/requests/book-api.js";
import fileAPI from "@/requests/file-api.js";
import adminAPI from "@/requests/admin-api.js";
import { Check, Edit, Close } from "@element-plus/icons-vue";
import { ElMessage } from 'element-plus'

const activeTab = ref("font");
const uploader = ref(false);
const previewer = ref(false);
const modifySysConfig = ref(false);
const previewFont = ref("");
const fontsData = ref([]);
const sysConfigs = ref([]);

const refreshFontsData = () => {
  bookAPI.getFonts().then((res) => {
    fontsData.value = res;
    const fontFaceCssList = [];
    res.forEach((font) => {
      if (font.url != null) {
        fontFaceCssList.push(`
        @font-face {
          font-family: "${font.fontValue}";
          src: url(${font.url});
        }
      `);
      }
    });
    const fontFaceCss = fontFaceCssList.join("\n");
    console.log(fontFaceCss);
    const styleTag = document.createElement("style");
    styleTag.setAttribute("type", "text/css");
    styleTag.appendChild(document.createTextNode(fontFaceCss));
    document.head.appendChild(styleTag);
  });
};

const refreshSysConfig = () => {
  adminAPI.requestConfigList().then((res) => {
    console.log(res);
    sysConfigs.value = res;
  });
};

onBeforeMount(() => {
  refreshFontsData();
  refreshSysConfig();
});

const form = reactive({
  fontName: "",
  fontValue: "",
  fontFile: null,
});
const handleExceed = (files) => {
  console.log(files[0]);
  console.log(form);
};
const handleFontChange = (file) => {
  console.log(file);
  form.fontFile = file.raw;
};

const uploadFont = () => {
  const formData = new FormData();
  formData.append("file", form.fontFile);
  fileAPI.uploadFile(formData).then((res) => {
    console.log(res);
    bookAPI
      .uploadFont({
        fontName: form.fontName,
        fontValue: form.fontValue,
        fontFileName: form.fontFile.name,
        url: res,
      })
      .then((res) => {
        ElMessage.success('上传成功');
        refreshFontsData();
        uploader.value = false;
      });
  });
};

const handlePreview = (index, row) => {
  previewFont.value = row.fontValue;
  previewer.value = true;
};

const handleDelete = (index, row) => {
  console.log(index, row);
  bookAPI.deleteFont(row.id).then((res) => {
    refreshFontsData();
  });
};

const saveSysConfig = (config) => {
  adminAPI.updateSysConfig(config).then((res) => {
    ElMessage.success('修改成功');
    modifySysConfig.value = false;
    refreshSysConfig();
  });
};
</script>

<style scoped>
.config-label {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.config-value {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
</style>
