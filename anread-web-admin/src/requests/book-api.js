import axios from "@/utils/axios.js";

const bookBaseURL = "/book-api";

const bookAPI = {
  getFonts: () => {
    return axios.get(`${bookBaseURL}/font`);
  },
  uploadFont: (data) => {
    return axios.post(`${bookBaseURL}/font`, data);
  },
  deleteFont: (id) => {
    return axios.delete(`${bookBaseURL}/font/${id}`);
  },
  
};

export default bookAPI;
