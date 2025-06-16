import axios from "axios";
import { AuthContext } from "../context/AuthContext";
import { useContext } from "react";
let refreshingFunc = undefined;

const instance = axios.create({
    baseURL: 'http://localhost:8080/',
    withCredentials: true, //cho phép gửi cookie Http-only khi gọi API
});

// Danh sách các endpoints không cần authentication
const publicEndpoints = [
    'ApiWebManga/auth', // Tất cả các endpoint bắt đầu bằng /auth/
    'ApiWebManga/books/getBook',
    'ApiWebManga/books/book-search-criteria',
    'ApiWebManga/books/book-search-keyword',
    'ApiWebManga/books/bookList',
    'ApiWebManga/chapter/getChapterById',
    'ApiWebManga/chapter/findPrevChapter',
    'ApiWebManga/chapter/findNextChapter',
    'ApiWebManga/chapter/findChaptersByBookId',
    'ApiWebManga/chapter/getRecentChaptersWithElapsedTime',
    'ApiWebManga/chapter/getLatestChapterAndTime',
    'ApiWebManga/chapter/getRecentChapterByBookWithElapsedTime',
    'ApiWebManga/category/getCategoryById',
    'ApiWebManga/comment/getCommentsByBook',
    'ApiWebManga/payment/vn-pay-callback'
];

// Kiểm tra xem một URL có phải là public endpoint không
const isPublicEndpoint = (url) => {
    const normalizedUrl = url.startsWith('/') ? url.slice(1) : url;

    return publicEndpoints.includes(normalizedUrl) ||
        (normalizedUrl.startsWith('ApiWebManga/auth/') && publicEndpoints.includes('ApiWebManga/auth'));
};


const refreshToken = async () => {
    try{
        const response = await instance.post('ApiWebManga/auth/refresh');//không gửi token,cookie tự động gửi
        if(response.status === 200){
            const accessToken = response.data.result.accessToken;
            sessionStorage.setItem("accessToken", accessToken);//refreshToken thành công thì lưu nó vào sesionStorage
            return accessToken;
        } else {
            throw new Error('Failed to refresh token');
        }
    } catch(error) {
        console.error("Error refreshing token: ", error);
        throw error;
    }
};

instance.interceptors.request.use(
    (config) => {
        const accessToken = sessionStorage.getItem('accessToken');
        
        // Chỉ thêm token vào header nếu:
        // 1. Có accessToken
        // 2. URL không phải là endpoint refresh token
        // 3. URL không phải là public endpoint
        if (accessToken && 
            config.url !== 'ApiWebManga/auth/refresh' && 
            !isPublicEndpoint(config.url)) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        
        return config;
    },
    (error) => {
        return error.response ? error.response : Promise.reject(error);
    }
);

instance.interceptors.response.use(
    (response) => {
        console.log(response);
        return response;//nếu api phản hồi thành công status !=401 thì trả về response như bình thường
    },
    async (error) => {
        const originalRequest = error.config;
        console.log(originalRequest);
        // Nếu là public endpoint và gặp lỗi 401, chỉ trả về lỗi mà không redirect
        if (error.response && error.response.status === 401 && isPublicEndpoint(originalRequest.url)) {
            console.log("isPublic:", isPublicEndpoint(originalRequest.url));
            console.log("originalRequest.url:", originalRequest.url);
            console.log("lỗi 401");
            return Promise.reject(error);
        }
        console.log(originalRequest);
        // Xử lý refresh token cho các endpoint yêu cầu authentication
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            
            if (!refreshingFunc) {
                refreshingFunc = refreshToken();
            }
            
            try {
                const newToken = await refreshingFunc;
                sessionStorage.setItem('accessToken', newToken);
                
                originalRequest.headers['Authorization'] = `Bearer ${newToken}`;
                refreshingFunc = undefined;
                
                return instance(originalRequest);
            } catch (refreshError) {
                refreshingFunc = undefined;
                sessionStorage.removeItem("accessToken");
                
                // Chỉ redirect đến login nếu không phải là public endpoint
                if (!isPublicEndpoint(originalRequest.url)) {
                    window.location = `${window.location.origin}/login`;
                }
                
                return Promise.reject(refreshError);
            }
        }
        
        return Promise.reject(error);
    }
);

export default instance;