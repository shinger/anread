import axios from "@/utils/axios.js";

const userBaseURL = "/user-api";

const userAPI = {
  userLogin: (data) => {
    return axios.post(`${userBaseURL}/user/login`, data);
  },
  getUserInfo: () => {
    return axios.get(`${userBaseURL}/user/info`);
  }
};

export default userAPI;
