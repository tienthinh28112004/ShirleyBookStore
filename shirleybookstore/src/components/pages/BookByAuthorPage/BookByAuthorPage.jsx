import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProfileInfo, updateProfile } from "../../../service/UserService";
import AuthContext from "../../../context/AuthContext";
import { TablePagination } from "@mui/material";
import { getAllBook, getAllBookByAuthor } from "../../../service/BookService";


export const BookByAuthor = () =>{
    const authContext = useContext(AuthContext);
    const accessToken = sessionStorage.getItem("accessToken");
    const [author,setAuthor]=useState(null);
    const [books,setBooks] = useState([]);//Lưu dánh sách truyện
    const [page,setPage] = useState(0);//Trang hiện tại
    const [rowsPerPage,setRowsPerPage]= useState(7);//Số dòng mỗi trang
    const [totalItems,setTotalItems] = useState(0);//Tổng số khóa học
    const [sort,setSort] = useState("id:desc");//Thứ tự sắp xếp
    const [searchTerm,setSearchTerm] = useState("");//Từ kháo tìm kiếm
    const navigate = useNavigate();//Hook điều hướng

    useEffect(() =>{
            fetchBooks(page + 1,rowsPerPage,searchTerm,sort);
        },[page,rowsPerPage,sort,searchTerm]);
    
    const fetchBooks = async (page,size,searchTerm,sort) =>{
        console.log(23)
        try{
            debugger
            const response= await getAllBookByAuthor(page,size,searchTerm,sort);
            debugger
            console.log(response);
            setBooks(response.result.items);
            setTotalItems(response.result.totalElements)
        }catch(error){
            console.error("Error fetching users:",error);
        }
    }
    console.log(books);
    useEffect(() => {
        const fetchAuthor = async () => {
          try {
            const response = await getProfileInfo();
            console.log(response)
            setAuthor(response.result);
          } catch (error) {
            console.error("Error fetching author:", error);
          }
        };
        fetchAuthor();
      }, []);      

    useEffect(() => {
        document.title="Book By Author";
    });
    const handleRowClick = (id) =>{
        navigate(`/book-detail/${id}`);//chuyển hướng tới đúng URL
    }

    // console.log(author);
    return (
        <div className="container" style={{marginTop:"210px"}}>
            <div className="row gutters">
                {/* Cột trái */}
                {author &&
                    <div className="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
                        <div className="card h-100">
                            <div className="card-body">
                                <label
                                    htmlFor="avatar"
                                    className="fw-bold fs-6 text-dark text-center d-flex justify-content-center"
                                >
                                    Ảnh đại diện
                                </label>
                                <div className="mb-3 d-flex justify-content-center">
                                    <img
                                        src={author.avatarUrl}
                                        alt="User Avatar"
                                        className="rounded-circle"
                                        style={{
                                            width: "150px",
                                            height: "150px",
                                            objectFit: "cover",
                                            border: "3px solid #62ab00",
                                        }}
                                    />
                                </div>
                                <div className="mb-3">
                                    <strong>Tác giả: </strong>{author.fullName}
                                </div>
                                <div className="mb-3">
                                    <strong>Email: </strong>{author.email}
                                </div>
                                <div className="mb-3">
                                    <strong>Số điện thoại: </strong>{author.phoneNumber}
                                </div> 
                                <div className="mb-3">
                                    <strong>Ngày sinh: </strong>{author.createdAt 
                                            ?new Date(author.createdAt).toLocaleString():"N/A"}
                                </div> 
                            </div>
                        </div>
                    </div>
                }
               

                {/* Cột phải */}
                <div className="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">
                    <div className="card h-100">
                        <div className="card-body">
                            <div className="book-manage">
                                <h2 className="book-manage-title">Book By Author</h2>
                                <div className="book-manage-controls">
                                    <div className="book-manage-search">
                                        <input 
                                            type="text"
                                            placeholder="Search by title or author"
                                            value={searchTerm}
                                            onChange={(e) => setSearchTerm(e.target.value)}
                                            onKeyDown={(e) =>{
                                                if(e.key === "Enter"){
                                                    setPage(0);
                                                    fetchBooks(1,rowsPerPage,searchTerm,sort);
                                                }
                                            }}
                                        />
                                    </div>
                                    <div className="book-manage-sort">
                                        <select onChange={(e) => setSort(e.target.value)}>
                                            <option value="title:asc">Sort by Title (A-Z)</option>
                                            <option value="title:desc">Sort By Title (Z-A)</option>
                                            <option value="id:desc">Sort by Date (Oldest)</option>
                                            <option value="id:asc">Sort by Date (Newest)</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="book-manage-table">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th className="table-icon">Title</th>
                                                <th className="table-icon">Category</th>
                                                <th className="table-icon">Price</th>
                                                <th className="table-icon">Created At</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {books.map((book,index) =>(
                                                <tr key={book.id}
                                                    onClick={() => handleRowClick(book.id)}
                                                    style={{cursor:"pointer"}}
                                                >
                                                    <td>{page*rowsPerPage+index+1}</td>
                                                    <td>{book.title}</td>
                                                    <td>{book.category}</td>
                                                    <td>{book.price} VND</td>
                                                    <td>{book.createdAt ?new Date(book.createdAt).toLocaleString():"N/A"}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                                <div className="pagination-container">
                                    <TablePagination 
                                        component="div"
                                        count={totalItems}
                                        page={page}
                                        onPageChange={(event,newPage) => setPage(newPage)}
                                        rowsPerPage={rowsPerPage}
                                        onRowsPerPageChange={(event) => 
                                            setRowsPerPage(parseInt(event.target.value,10))
                                        }
                                        rowsPerPageOptions={[7,14,21]}
                                        //className="custom-pagination"
                                        labelDisplayedRows={({from,to,count}) =>`${from}-${to} of ${count}`}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}