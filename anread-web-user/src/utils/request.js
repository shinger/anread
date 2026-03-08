import axios from "./axios";
import md5 from "js-md5";

const baseURL = {
  user: "/user-api",
  filesystem: "/file-api",
  book: "/book-api",
};

const request = {
  // 用户服务接口
  userRegister: (data) => {
    return axios.post(`${baseURL.user}/user/register`, {
      username: data.username,
      email: data.email,
      password: md5(data.password),
    });
  },
  userLogin: (data) => {
    return axios.post(`${baseURL.user}/user/login`, {
      email: data.email,
      password: md5(data.password),
    });
  },
  getRecommends: () => {
    return axios.get(`${baseURL.book}/recommend/common`);
  },
  getUserInfo: () => {
    return axios.get(`${baseURL.user}/user/info`);
  },
  uploadAvatar: (formData) => {
    return axios.post(`${baseURL.user}/user/avatar`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  uploadUserAnnotation: (data) => {
    return axios.post(`${baseURL.user}/user/annotation/upload`, {
      userId: data.userId,
      bookId: data.bookId,
      epubCfiRange: data.cfiRange,
      type: data.type,
      lineColor: data.lineColor,
    });
  },
  // 图书服务接口
  getShelfBooks: (pageNum) => {
    return axios.get(`${baseURL.book}/bookshelf/list?t=${Date.now()}`, {
      params: { pageNum: pageNum },
    }); // 防缓存
  },
  getHomeShelfBooks: () => {
    return axios.get(`${baseURL.book}/bookshelf/list_8?t=${Date.now()}`, {
      params: { pageNum: 1 },
    }); // 防缓存
  },
  uploadReadingRecord: (data) => {
    return axios.post(`${baseURL.book}/reading/record/upload`, {
      bookId: data.bookId,
      lastReadCfi: data.lastReadCfi,
      readingProgress: data.readingProgress,
      readingDuration: data.readingDuration,
    });
  },
  getBookInfo: (id) => {
    return axios.get(`${baseURL.book}/book/${id}`);
  },
  getReadingRecord: (id) => {
    return axios.get(`${baseURL.book}/reading/record/${id}`);
  },
  getBookEpub: (id) => {
    return axios.get(`${baseURL.book}/book/epub/${id}`);
  },
  getCategories: (id) => {
    return axios.get(`${baseURL.book}/categories/${id}`);
  },
  getCategoryBooks: (id) => {
    return axios.get(`${baseURL.book}/book/category/${id}`);
  },
  getIsInShelf: (id) => {
    return axios.get(`${baseURL.book}/bookshelf/inshelf/${id}`);
  },
  addToShelf: (id) => {
    return axios.post(`${baseURL.book}/bookshelf/join/${id}`);
  },
  removeFromShelf: (id) => {
    return axios.delete(`${baseURL.book}/bookshelf/remove/${id}`);
  },
  markFinished: (id) => {
    return axios.post(`${baseURL.book}/reading/record/finished/${id}`);
  },
  getFonts: () => {
    return axios.get(`${baseURL.book}/font`);
  },
};

export default request;
