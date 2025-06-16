
import axios from "../utils/CustomizeAxios";

export const createOrder = async (fullName,phoneNumber,address,note,paymentExpression,detailRequests)=>{

    try{
        const response = await axios.post(`ApiWebManga/order/createOrder`,{
            fullName:fullName,
            phoneNumber:phoneNumber,
            address:address,
            note:note,
            paymentExpression:paymentExpression,
            detailRequests:detailRequests
        });
        return response.data;
    }catch(error){
        console.log('Failed to getNotification By Current Login',error);
        throw error;
    }
}

export const getOrderById = async (orderId)=>{
    try{
        const response = await axios.get(`ApiWebManga/order/getOrder/${orderId}`);
        console.log(response)
        return response.data;
    }catch(error){
        console.log('Failed to markAsReadNotification',error);
        throw error;
    }
}

export const getOrderByUser = async ()=>{
    try{
        const response = await axios.get(`ApiWebManga/order/getHistoryOrder`);
        console.log(response)
        return response.data;
    }catch(error){
        console.log('Failed to markAsReadNotification',error);
        throw error;
    }
}

export const changeStatus = async (orderId,status)=>{//đây là patch nên gửi param có phần đặc biệt
    try{
        const response = await axios.patch(`ApiWebManga/order/${orderId}/orderStatus`,
            {},//Với các phương thức HTTP có thể chứa body (POST, PUT, PATCH), cú pháp axios yêu cầu tham số thứ hai phải là dữ liệu cho body. 
            // //Nếu bạn không cần gửi dữ liệu trong body, bạn vẫn phải truyền một giá trị như {} hoặc null để giữ đúng vị trí của tham số.
            {
            params:{
                newStatus:status
            }
        }  
        );
        // console.log(response)
        return response.data;
    }catch(error){
        console.log('Failed to markAsReadNotification',error);
        throw error;
    }
}

export const getListOrders = async (page,size)=>{
    try{
        const response = await axios.get(`ApiWebManga/order/getOrderRecent`,{
            params:{
                page:page,
                size:size
            }
            }  
        );

        return response.data;
    }catch(error){
        console.log('Failed to markAsReadNotification',error);
        throw error;
    }
}