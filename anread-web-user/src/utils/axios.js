import axios from "axios";
import { useRouter } from "vue-router";
import router from "@/router/index";
import { useToast } from "vue-toast-notification";

const toast = useToast();

// 修改baseURL：开发环境用/api（走vite代理），生产环境用完整IP
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL
// axios.defaults.baseURL = '/api'

axios.defaults.withCredentials = true;
axios.defaults.headers["X-Requested-With"] = "XMLHttpRequest";
axios.defaults.headers.post["Content-Type"] = "application/json";

// 请求拦截器（不变）
axios.interceptors.request.use(
  function (config) {
    if (window.localStorage.token) {
      config.headers.Authorization = localStorage.getItem("token");
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// 响应拦截器（不变，仅优化错误提示）
axios.interceptors.response.use((res) => {
  console.log("data:", res.data);
  if (res.data == null || res.data.code == null) {
    return res.data;
  }
  if (res.data.code != "200") {
    if (res.data.message) toast.error(res.data.message);
    if (res.data.code == "401") {
      localStorage.removeItem("token");
      router.push({ path: "/login" });
    }
    return Promise.reject(res.data);
  }
  return res.data.data;
}, error => {
  console.error("接口请求错误：", error.message, "请求地址：", error.config?.url); // 新增：打印请求地址
  if (error.response == undefined) {
    toast.error("网络请求失败，请检查后端服务或网络"); // 新增：友好提示
    return Promise.reject(error); // 新增：确保Promise有返回
  }
  if (error.response.status == 401) {
    localStorage.removeItem("token");
    router.push({ path: "/login" });
  }
  return Promise.reject(error);
});

export default axios;