import axios from "../utils/CustomizeAxios";

export const createPayment = async (amount) => {
    try {
        const response = await axios.post(`ApiWebManga/payment/create_payment`,{
           amount:amount
        })
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}
//cái này backend dùng là chủ yếu
export const handleVnpayCallback = async () => {
    try {
        const response = await axios.get(`ApiWebManga/payment/vn-pay-callback`);
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}