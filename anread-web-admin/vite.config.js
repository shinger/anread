import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import path from "path";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import ElementPlus from "unplugin-element-plus/vite"; // 不加这个配置，ElMessage出不来

// https://vitejs.dev/config/
export default ({ mode }) =>
  defineConfig({
    plugins: [
      vue(),
      Components({
        resolvers: [
          ElementPlusResolver({
            importStyle: "sass",
          }),
        ],
      }),
      ElementPlus(),
    ],
    resolve: {
      alias: {
        "~": path.resolve(__dirname, "./"),
        "@": path.resolve(__dirname, "src"),
      },
    },
    base: "./",
    server: {
      proxy: {
        "^/[^/]+-api": {
          // target: "http://127.0.0.1:8000",
          target: "http://host.docker.internal:8000",
          changeOrigin: true,
        },
      },
      host: "0.0.0.0",
      port: 5174,
      strictPort: true,
    },

    css: {
      preprocessorOptions: {
        // 覆盖掉element-plus包中的主题变量文件
        scss: {
          additionalData: `@use "@/styles/element/index.scss" as *;`,
        },
      },
    },
    define: {
      // enable hydration mismatch details in production build
      __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: "true",
    },
  });
