import axios from "../utils/CustomizeAxios";

export const addItemCart = async (bookId,quantity=1) => {
    try {
        const response = await axios.post(`ApiWebManga/cart/addOrUpdateItem`,{
            bookId:bookId,
            quantity:quantity
        })
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const deleteItemcart = async (bookId) => {
    try {
        const response = await axios.delete(`ApiWebManga/cart/delete/${bookId}`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}


export const deleteAllItemcart = async () => {
    try {
        const response = await axios.delete(`ApiWebManga/cart/deleteAll`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}


export const detailcart = async () => {
    try {
        const response = await axios.get(`ApiWebManga/cart/detailCart`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}