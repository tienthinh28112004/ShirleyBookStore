import { toast } from "react-toastify";
import axios from "../utils/CustomizeAxios";

export const getCommentByBook = async(bookId,currentPage)=>{
    try{
        const response= await axios.get(`ApiWebManga/comment/getCommentsByBook/${bookId}`,{
            params:{
                page: currentPage
            }
        });
        return response.data;
    }catch(error){
        console.error(`Error fetching comments from API:`,error.response || error.message);
        throw new Error(`Failed to fetch comments: ${error.response?.status || error.message}`);
    }
};

export const addComment = async(commentData) =>{
    try{
        const response = await axios.post(`ApiWebManga/comment/insertComment`,commentData);
        console.log(response);
        if(response.data.result){
            toast.success('Comment add successfully');
            return response.data.result;
        }else{
            toast.error(response.data.message);
            throw new Error(response.data.message);
        }
    }catch(error){
        console.error('Error in addComment:',error);
        throw error;
    }
}

export const replyComment = async(replyData) =>{
    try{
        const response = await axios.post(`ApiWebManga/comment/insertComment`,replyData);
        console.log(response);
        if(response.data.result){
            toast.success('Comment add successfully');
            return response.data.result;
        }else{
            toast.error(response.data.message);
            throw new Error(response.data.message);
        }
    }catch(error){
        console.error('Error in addComment:',error);
        throw error;
    }
}

export const deleteComment = async (commentId) =>{
    try{
        await axios.delete(`ApiWebManga/comment/deleteComment/${commentId}`);
    }catch (error){
        console.error(error);
        throw error;
    }
}

export const updateComment = async (commentId,updateContent)=>{
    try{
        const response = await axios.put(`ApiWebManga/comment/updateComment/${commentId}`,
            {content:updateContent}
        )
        if(response.data.result){
            toast.success('Update comment successfully');
            return response.data.result;
        }else{
            toast.error(response.data.message);
            throw new Error(response.data.message);
        }
    }catch(error){
        console.error('Error in service:',error);
        throw error;
    }
}