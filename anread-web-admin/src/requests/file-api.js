import axios from "@/utils/axios.js";

const fileBaseURL = "/file-api";

const fileAPI = {
  uploadFile: (formData) => {
    return axios.post(`${fileBaseURL}/file/common`, formData);
  },
  uploadBookFile: (formData) => {
    return axios.post(`${fileBaseURL}/file/book`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }
};

export default fileAPI;
