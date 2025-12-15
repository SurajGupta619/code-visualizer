import axios from "axios";

const TOKEN_KEY = "token";
// Create axios instance
const http = axios.create({
  baseURL: "http://192.168.0.110:8081", // set in .env if you have a backend
  // baseURL: "10.23.247.156:8081", // set in .env if you have a backend
  headers: { "Content-Type": "application/json" }
});

// Request interceptor: attach token if present
http.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

// Response interceptor: global 401 handling
http.interceptors.response.use(
  (res) => res,
  (error) => {
    if (error.response && error.response.status === 403) {
      // token invalid/expired — clear and redirect to login
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem("userName");
      // Use window.location so this file has no router dependency
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default http;