import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index";
import { localGet, localRemove } from "./index";

// 这边由于后端没有区分测试和正式，姑且都写成一个接口。
// axios.defaults.baseURL = config[import.meta.env.MODE].baseUrl
// 携带 cookie，对目前的项目没有什么作用，因为我们是 token 鉴权
axios.defaults.withCredentials = true;
// 请求头，headers 信息
axios.defaults.headers["X-Requested-With"] = "XMLHttpRequest";
axios.defaults.headers["Authorization"] = localGet("token") || "";
// 默认 post 请求，使用 application/json 形式
axios.defaults.headers.post["Content-Type"] = "application/json";
console.log("axios is loaded");

// 请求拦截器，内部根据返回值，重新组装，统一管理。
axios.interceptors.response.use(
  (res) => {
    console.log(res);
    if (res.data.code != 200) {
      if (res.data.message) ElMessage.error(res.data.message);
      if (res.data.code == 401) {
        router.push({ path: "/login" });
      }
      return Promise.reject(res.data);
    }
    return res.data.data;
  },
  (error) => {
    console.log(error);
    if (error.response && error.response.status === 401) {
      localRemove("token");
      router.push({ path: "/login" });
      ElMessage.error("登录过期，请重新登录");
    }

    return Promise.reject(error);
  }
);

export default axios;
