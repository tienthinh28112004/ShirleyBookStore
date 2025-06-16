import axios from "../utils/CustomizeAxios";

// export const getAllBook = async (currentPage=1,pageSize=10,) => {
//     try {
//         const response = await axios.get(`ApiWebManga/books/bookList`,{
//             params:{
//                 page: currentPage,
//                 size: pageSize
//             }
//         });
//         return response.data;
//     } catch (error) {
//         console.error('Error get my info', error);
//         throw error;
//     }
// }
export const getAllBook = async (page,size,keyword,sorts) =>{
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
        
        const response = await axios.get('ApiWebManga/books/bookList', {
            params: params
        });
        return response.data;
    }catch(error){
        throw error;
    }
}

export const getAllBookByAuthor = async (page,size,keyword,sorts) =>{
  try{
      console.log(12);
      console.log({page,size,keyword,sorts})
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
      console.log(params)
      const response = await axios.get('ApiWebManga/books/bookListByAuthor', {
          params: params
      });
      console.log(response.data)
      return response.data;
  }catch(error){
      throw error;
  }
}

export const SearchBook = async (currentPage, pageSize, sortBy, authorName, listSearch = []) => {
    try {
        const apiUrl = `ApiWebManga/books/book-search-criteria`;

        // Tạo URLSearchParams để đảm bảo params được encode đúng cách
        const params = new URLSearchParams();
        params.append('page', currentPage);
        params.append('size', pageSize);
        console.log(currentPage);
        console.log(pageSize)
        console.log(sortBy)//
        console.log(authorName)//
        console.log(listSearch)
        if (sortBy) {
            params.append('sortBy', sortBy);
            console.log(sortBy);
        }

        if (authorName) {
            params.append('authorName', authorName);
        }

        console.log("Chưa qua nổi listSearch",listSearch)
        if(listSearch){
            //Append từng search item riêng biệt (sẽ ra search=title:conan&search=category:Trinh Thám)
            listSearch.forEach(item => params.append('search', item));
        }
        console.log("qua list search rồi")
        console.log("param search",params);

        const response = await axios.get(apiUrl, { params });
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.error('Error fetching books:', error);
        throw error;
    }
};


export const getBookById = async (id) =>{
    try{
        const response = await axios.get(`ApiWebManga/books/getBook/${id}`);
        return response.data;
    }catch(error){
        console.error('Error fetching chapter by id:',error);
        throw error;
    }
}

export const toggleStatusBook = async (id) =>{
  try{
      const response = await axios.patch(`ApiWebManga/books/toggleStatus/${id}`);
      return response.data;
  }catch(error){
      console.error('Error fetching chapter by id:',error);
      throw error;
  }
}

export const uploadBook = async (bookData, thumbnailFile) => {
    try {
      // Create a FormData object
      const formData = new FormData();
      
      // Append the JSON request data
      formData.append('request', new Blob([JSON.stringify(bookData)], {
        type: 'application/json'
      }));
      
      // Append the thumbnail file
      formData.append('thumbnail', thumbnailFile);
      
      // Note: We're not including the PDF file
      
      const response = await axios.post(`ApiWebManga/books/upload-books`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      return response.data;
    } catch (error) {
      console.error('Error uploading book:', error);
      throw error;
    }
  };

  
export const AdminUploadBook = async (bookData, thumbnailFile) => {
    try {
      // Create a FormData object
      const formData = new FormData();
      
      // Append the JSON request data
      formData.append('request', new Blob([JSON.stringify(bookData)], {
        type: 'application/json'
      }));
      
      // Append the thumbnail file
      formData.append('thumbnail', thumbnailFile);
      
      // Note: We're not including the PDF file
      
      const response = await axios.post(`ApiWebManga/books/admin/upload-books`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      return response.data;
    } catch (error) {
      console.error('Error uploading book:', error);
      throw error;
    }
  };