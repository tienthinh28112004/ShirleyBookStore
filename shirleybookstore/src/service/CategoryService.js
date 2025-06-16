import axios from "../utils/CustomizeAxios";

export const createCategory = async (name,description) => {
    try {
        const response = await axios.post(`ApiWebManga/category/insertCategory`,{
            name:name,
            description:description
        })
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const getCategoryById = async (categoryId) => {
    try {
        const response = await axios.get(`ApiWebManga/category/getCategoryId/${categoryId}`);
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const listAllCategory = async () => {
    try {
        const response = await axios.get(`ApiWebManga/category/listCategory`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const deleteCategory = async (categoryId) => {
    try {
        const response = await axios.delete(`ApiWebManga/category/deleteCategory/${categoryId}`);
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}