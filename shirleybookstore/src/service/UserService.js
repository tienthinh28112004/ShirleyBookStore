

// export const getMyInfo = async () => {
//     try {
//         const response = await axios.get(`api/v1/my-info`)
//         return response.data;
//     } catch (error) {
//         console.error('Error get my info', error);
//         throw error;
//     }
// }

import axios from "../utils/CustomizeAxios";

export const createUser = async (fullName,email,password) => {
    try {
        const response = await axios.post(`ApiWebManga/users/addUser`,{
            fullName:fullName,
            email:email,
            password:password
        })
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const getUserById = async (userId) => {
    try {
        const response = await axios.get(`ApiWebManga/users/${userId}`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const banUser = async (userId) => {
    try {
        const response = await axios.patch(`ApiWebManga/users/banUser/${userId}`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const unBanUser = async (userId) => {
    try {
        const response = await axios.patch(`ApiWebManga/users/unBanUser/${userId}`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const getAllUser = async (page,size,keyword,sorts) =>{
    try{
        
        const params = new URLSearchParams();
        params.append('page', page);
        params.append('size', size);
        
        if (keyword) {
            params.append('keyword', keyword);
        }
        
        // Add each sort parameter separately
        if (sorts) {
            params.append('sorts',sorts);
        }
        console.log(params);
        console.log({page,size,keyword,sorts})
        
        const response = await axios.get('ApiWebManga/users/list-with-sort-by-multiple-columns', {
            params: params
        });
        return response.data;
    }catch(error){
        throw error;
    }
}

export const updateRoleAuthor = async (userId) => {
    try {
        const response = await axios.patch(`ApiWebManga/users/updateRoleAuthor/${userId}`)
        console.log(response);
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const banAuthor = async (userId) => {
    try {
        const response = await axios.patch(`ApiWebManga/users/banAuthor/${userId}`)
        return response.data;
    } catch (error) {
        console.error('Error get my info', error);
        throw error;
    }
}

export const getProfileInfo = async () => {
    const response = await axios.get(`ApiWebManga/users/myInfo`)
    return response.data;
}


export const updateProfile = async (profileData) => {
    try {
        const response = await axios.patch(`ApiWebManga/users/update`, profileData);
        return response.data;
    } catch (error) {
        console.log('Fail to update profile', error)
        throw error;
    }
};

export const getAuthorDetails = async (authorId) => {
  try {
    const response = await axios.get(`ApiWebManga/users/${authorId}/details`,);
    return response.data;
  } catch (error) {
    console.error("Error fetching user details:", error);
    throw error;
  }
};