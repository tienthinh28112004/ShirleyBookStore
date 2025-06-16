import axios from "../utils/CustomizeAxios";

export const registerAuthor = async (formDataToSend) => {
    try {
        const response = await axios.post(`ApiWebManga/registerAuthor/register-author`, formDataToSend, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data;
    } catch (error) {
        console.error('Error in registerTeacher API:', error);
        throw error;
    }
};

export const registerList = async () => {
    try {
        const response = await axios.get(`ApiWebManga/registerAuthor/registration-authors`);
        return response.data;
    } catch (error) {
        console.error('Error in registerTeacher API:', error);
        throw error;
    }
};