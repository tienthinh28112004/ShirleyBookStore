import { useEffect, useState } from "react";
import { FaUpload, FaBook, FaTags, FaDollarSign, FaFileAlt, FaClock, FaImage } from 'react-icons/fa';
import { createCategory, listAllCategory } from "../../../../service/CategoryService";
import { Select } from "antd";
import TinyMCE from "../../../../utils/TinyMCE";
export const UploadCategory = () =>{
    const [categoryName,setCategoryName] = useState('');
    const [categoryDescription,setCategoryDescription]= useState('');

    useEffect(() =>{
        document.title = "Create a Category"
    })

    // Hàm reset form về giá trị mặc định
    const resetForm = () => {
        setCategoryName("");
        setCategoryDescription("");
    };

    const handleSubmit = async (e) =>{
        e.preventDefault();
        try {
            const result = await createCategory(categoryName,categoryDescription);

            resetForm();
            
          } catch (error) {
            console.error("Upload failed:", error);
            // Handle error - display message to user
          }
    };
    return (
        <div className="upload-book-container">
            <div className="container upload-container my-5">
                <div className="row justify-content-center">
                    <div className="col-lg-7 col-md-9">
                        {/* <div className="card shadow-lg border-0 rounded-4 bg-light"> */}
                            <div className="card-body p-5">
                                <h3 className="card-title text-center mb-4 text-dark fw-bold">
                                    Upload New Book
                                </h3>
                                <form onSubmit={handleSubmit}>
                                    <div className="mb-4">
                                        <label htmlFor="categoryName" className="form-label fs-5 text-dark fw-semibold">
                                            Tên danh mục
                                        </label>
                                        <input 
                                            type="text"
                                            className="form-control shadow-sm"
                                            id="categoryName"
                                            placeholder="Enter the name of the category"
                                            value={categoryName}
                                            onChange={(e) =>setCategoryName(e.target.value)}
                                        />
                                    </div>
                                    <div className="mb-4">
                                        <label htmlFor="categoryDescription" className="form-label fs-5 text-dark fw-semibold">
                                            Mô tả danh mục
                                        </label>
                                       <TinyMCE value={categoryDescription} onChange={setCategoryDescription}/>
                                    </div>
                                    <div className="d-grid btn-block">
                                        <button type="submit" className="btn btn-lg shadow-sm upload-btn">
                                            <FaUpload className="me-2"/> Create category
                                        </button>
                                    </div>
                                </form>
                            </div>
                        {/* </div> */}
                    </div>
                </div>
            </div>
        </div>
    )
}