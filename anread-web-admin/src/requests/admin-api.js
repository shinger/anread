import axios from "@/utils/axios.js";

const adminBaseURL = "/admin-api";

const adminAPI = {
  // 获取书籍列表
  requestBooks: (params) => {
    return axios.get(`${adminBaseURL}/book/list`, params);
  },
  // 获取书籍详情
  requestBookDetail: (id) => {
    return axios.get(`${adminBaseURL}/book/${id}`);
  },
  uploadBook: (params) => {
    return axios.post(`${adminBaseURL}/book`, params);
  },
  updateBook: (params) => {
    return axios.put(`${adminBaseURL}/book`, params);
  },
  deleteBook: (id) => {
    return axios.delete(`${adminBaseURL}/book/${id}`);
  },
  updateBookStatus: (id, status) => {
    return axios.put(`${adminBaseURL}/book/${id}/${status}`);
  },
  /* 书籍分类API */
  requestCategories: (params) => {
    return axios.get(`${adminBaseURL}/categories`, { params });
  },
  requestCategoryDetail: (id) => {
    return axios.get(`${adminBaseURL}/categories/${id}`);
  },
  updateCategory: (params) => {
    return axios.put(`${adminBaseURL}/categories`, params);
  },
  addCategory: (params) => {
    return axios.post(`${adminBaseURL}/categories`, params);
  },
  /* 系统配置API */
  requestConfigList: () => {
    return axios.get(`${adminBaseURL}/sys_config/list`);
  },
  updateSysConfig: (params) => {
    return axios.put(`${adminBaseURL}/sys_config`, params);
  }

};

export default adminAPI;
