/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import { TablePagination } from "@mui/material";
import { deleteCategory, listAllCategory } from "../../../../service/CategoryService";
import "../../css/OrderManage.css";

const CatgoryManage = ()=>{
    const [listCategory,setListCategory]=useState([]);

    useEffect(() => {
        fetchCategories();
    },[]);

    const fetchCategories = async() =>{
        try{
            const response= await listAllCategory();
            setListCategory(response.result);
        }catch(error){
            console.error("Error fetching orders:",error)
        }
    };
    const handleDeleteCategory = async (id) => {
        try {
            const response = await deleteCategory(id);
            // Sau khi xóa thành công, cập nhật danh sách:
            setListCategory(prevList => prevList.filter(category => category.id !== id));
        } catch (error) {
            console.error("Error deleting category:", error);
        }
    };
    
    return (
        <div className="order-manage">
            <h2 className="order-manage-title">Order Management</h2>
            <div className="order-manage-table">
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th className="table-icon">Name</th>
                            <th className="table-icon">Description</th>
                            <th className="table-icon">Delete Category</th>
                        </tr>
                    </thead>
                    <tbody>
                        {listCategory.map((category,index) =>(
                            <tr key={index}>
                                <td>{index+1}</td>
                                <td >{category.name}</td>
                                <td><div dangerouslySetInnerHTML={{ __html:category.description }} /></td>
                                <td>
                                    <button
                                        onClick={() => handleDeleteCategory(category.id)}
                                        style={{
                                            backgroundColor: "#e74c3c",
                                            color: "white",
                                            padding: "5px 10px",
                                            borderRadius: "5px",
                                            cursor: "pointer"
                                        }}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CatgoryManage;