import axios from "../utils/CustomizeAxios";

export const notificationCurrentLogin = async ()=>{
    try{
        const response = await axios.get(`ApiWebManga/notification/getNotificationsForCurrentUser`);
        return response.data;
    }catch(error){
        console.log('Failed to getNotification By Current Login',error);
        throw error;
    }
}

export const markAsReadNotification = async (notificationId)=>{
    try{
        const response = await axios.put(`ApiWebManga/notification/markAsRead/${notificationId}`);
        console.log(response)
        return response.data;
    }catch(error){
        console.log('Failed to markAsReadNotification',error);
        throw error;
    }
}