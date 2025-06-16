import axios from "../utils/CustomizeAxios";

export const getFavorite = async (currentPage) =>{
    try{
        const response = await axios.get(`ApiWebManga/favorite/favoriteUserCurrent`,{
            params:{
                page: currentPage
            }
        })
        return response.data;
    }catch(error){
        console.log('Fail to get Favorite',error);
        throw error;
    }
}

export const addFavorite = async (bookId) =>{
    try{
        const response = await axios.post(`ApiWebManga/favorite/createFavorite`,{
            bookId: bookId
        });
        console.log(response);
        return response;
    }catch(error){
        console.error('Failed to add to favorites',error);
        throw error;
    }
}

export const removeFavorite = async (favoriteId)=>{
    try{
        const response = await axios.delete(`ApiWebManga/favorite/deleteFavorite/${favoriteId}`);
        return response;
    }catch(error){
        console.log('Failed to delete favorite',error);
        throw error;
    }
}
