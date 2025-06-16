/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import { getAllBook, SearchBook, toggleStatusBook } from "../../../../service/BookService";
import { TablePagination } from "@mui/material";
import "../../css/BookManage.css";


const BookManage = ()=>{
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
        try{

            const response= await getAllBook(page,size,searchTerm,sort);

            setBooks(response.result.items);
            setTotalItems(response.result.totalElements)
        }catch(error){
            console.error("Error fetching users:",error);
        }
    }

    // useEffect(() => {
    //     fetchBooks();
    // },[page,rowsPerPage,sort,searchTerm]);

    // const fetchBooks = async() =>{
    //     try{
    //         console.log({page,rowsPerPage,sort,searchTerm});
    //         const listSearch=[];
    //         if(searchTerm){
    //             listSearch.push(`title:${searchTerm}`)
    //         }
            
    //         const response= await SearchBook(page+1,rowsPerPage,sort,"",listSearch);
    //         console.log(response);
    //         setBooks(response.result.items);//lưu danh sách khóa học
    //         setTotalItems(response.totalElements);//Lưu tổng số khóa học
    //     }catch(error){
    //         console.error("Error fetching books:",error)
    //     }
    // };
    
    //Xử lý điều kiện tìm kiếm
    // const handleSerachKeyDown = (e) =>{
    //     if(e.key === "Enter"){
    //         fetchBooks();
    //     }
    // };

    //Điều hướng đến trang chi tiết khóa học
    const handleRowClick = (id) =>{
        navigate(`/admin/book/detail/${id}`);//chuyển hướng tới đúng URL
    }

    const handleToggleBookStatus = async(bookId,isActive) =>{
            try{
                const book = books.find((book) => book.id === bookId);//lấy thông tin dựa trên userId
                await toggleStatusBook(bookId);
                console.log(book);

                setBooks((prevBooks) =>
                    prevBooks.map((book) =>
                        book.id===bookId ? {...book,isActive: !isActive} : book
                    )
                );
            }catch(error){
                console.error(`Error toggling ban status for user ${bookId}`,error);
            }
        };
    // const handleToggleBookStatus = async(bookId) =>{
    //         try{
    //             const book = books.find((book) => book.id === bookId);//lấy thông tin dựa trên bookId
    //             await handleToggleBookStatus(bookId);
    //             setBooks((prevBooks) =>
    //                 prevBooks.map((book) =>
    //                     book.id===bookId ? {...book,active: !book.active} : book
    //                 )
    //             );
    //         }catch(error){
    //             console.error(`Error toggling ban status for user ${bookId}`,error);
    //         }
    //     }
    return (
        <div className="book-manage">
            <h2 className="book-manage-title">Book Management</h2>
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
                            <th className="table-icon">Author</th>
                            <th className="table-icon">Category</th>
                            <th className="table-icon">Price</th>
                            <th className="table-icon">Created At</th>
                            <th className="table-icon">Đăng truyện</th>
                            {/* <th className="table-icon">Updated At</th> */}
                        </tr>
                    </thead>
                    <tbody>
                        {books.map((book,index) =>(
                            <tr key={book.id}>
                                <td>{page*rowsPerPage+index+1}</td>
                                <td
                                    onClick={() => handleRowClick(book.id)}
                                    style={{cursor:"pointer"}}
                                    >{book.title}
                                </td>
                                <td
                                    onClick={() => handleRowClick(book.id)}
                                    style={{cursor:"pointer"}}
                                    >{book.authorName || "Unknown"}
                                </td>
                                <td
                                    onClick={() => handleRowClick(book.id)}
                                    style={{cursor:"pointer"}}
                                    >{book.category}
                                </td>
                                <td
                                    onClick={() => handleRowClick(book.id)}
                                    style={{cursor:"pointer"}}
                                    >{book.price} VND
                                </td>
                                <td
                                    onClick={() => handleRowClick(book.id)}
                                    style={{cursor:"pointer"}}
                                    >{book.createdAt 
                                        ?new Date(book.createdAt).toLocaleString()
                                        :"N/A"}
                                </td>
                                <td>
                                    <label className="switch">
                                        <input 
                                            type="checkbox"
                                            checked={book.isActive}
                                            onChange={() =>
                                                handleToggleBookStatus(book.id,book.isActive)
                                            }    
                                        />
                                        <span className="slider round">
                                            {book.isActive ? "Yes":"No"}
                                        </span>
                                    </label>
                                </td>
                                {/* <td>
                                    {book.updatedAt
                                        ?new Date(book.updatedAt).toLocaleString()
                                        :"N/A"}
                                </td> */}
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
    );
};

export default BookManage;