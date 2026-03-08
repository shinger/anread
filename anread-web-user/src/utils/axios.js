import axios from "axios";
import { useRouter } from "vue-router";
import router from "@/router/index";
import { useToast } from "vue-toast-notification";

const toast = useToast();
// const router = useRouter();

axios.defaults.baseURL = "/api";
// 携带 cookie，对目前的项目没有什么作用，因为我们是 token 鉴权
axios.defaults.withCredentials = true;
// 请求头，headers 信息
axios.defaults.headers["X-Requested-With"] = "XMLHttpRequest";
// 默认 post 请求，使用 application/json 形式
axios.defaults.headers.post["Content-Type"] = "application/json";

// 添加请求拦截器
axios.interceptors.request.use(
  function (config) {
    // 添加请求头
    if (window.localStorage.token) {
      config.headers.Authorization = localStorage.getItem("token");
    }
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 响应拦截器，内部根据返回值，重新组装，统一管理。
axios.interceptors.response.use((res) => {
  // console.log(res.data);

  if (res.data === undefined || typeof res.data !== "object") {
    return Promise.reject(res);
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
  console.log(error);
  if (error.response == undefined) {
    return;
  }
  if (error.response.status == 401) {
    localStorage.removeItem("token");
    router.push({ path: "/login" });
  }
  return Promise.reject(error);
});

export default axios;
