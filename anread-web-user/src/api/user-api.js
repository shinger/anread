import axios from "../utils/axios";
import md5 from "js-md5";

const baseURL = "/user-api";

// 用户注册
const userRegister = (data) => {
  return axios.post(`${baseURL}/user/register`, {
    username: data.username,
    email: data.email,
    password: md5(data.password),
  });
};

// 用户登录
const userLogin = (data) => {
  return axios.post(`${baseURL}/user/login`, {
    email: data.email,
    password: md5(data.password),
  });
};

// 获取推荐书单
const getRecommends = () => {
  return axios.get(`${baseURL}/book/recommend/common`);
};

// 获取用户信息
const getUserInfo = () => {
  return axios.get(`${baseURL}/user/info`);
};

// 上传用户头像
const uploadAvatar = (formData) => {
  return axios.post(`${baseURL}/user/avatar`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

// 上传用户划线
const uploadUserAnnotation = (data) => {
  return axios.post(`${baseURL}/user/annotation/upload`, {
    userId: data.userId,
    bookId: data.bookId,
    epubCfiRange: data.cfiRange,
    type: data.type,
    lineColor: data.lineColor,
    lineContent: data.lineContent,
    ideaContent: data.ideaContent,
    tocParentIndex: data.tocParentIndex,
    tocChildIndex: data.tocChildIndex,
  });
};

// 获取用户划线
const getUserAnnotations = (bookId) => {
  return axios.get(`${baseURL}/user/annotation/${bookId}`);
}

// 删除用户划线
const deleteUserAnnotation = (id) => {
  return axios.delete(`${baseURL}/user/annotation/${id}`);
}

// 更新用户划线
const updateUserAnnotation = (data) => {
  return axios.put(`${baseURL}/user/annotation`, {
    id: data.id,
    lineColor: data.lineColor,
    lineContent: data.lineContent,
    ideaContent: data.ideaContent,
  })
}

// 获取公开想法位置
const getPublicIdeaPos = (bookId) => {
  return axios.get(`${baseURL}/user/annotation/public/${bookId}`);
}

// 获取公开想法内容
const getPublicIdeas = (bookId, epubCfiRange) => {
  return axios.get(`${baseURL}/user/annotation/public/ideas?bookId=${bookId}&epubCfiRange=${epubCfiRange}`);
}


export {
  userRegister,
  userLogin,
  getRecommends,
  getUserInfo,
  uploadAvatar,
  uploadUserAnnotation,
  getUserAnnotations,
  deleteUserAnnotation,
  updateUserAnnotation,
  getPublicIdeaPos,
  getPublicIdeas,
};
