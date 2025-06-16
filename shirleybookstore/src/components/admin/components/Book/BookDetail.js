/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom"
import { getBookById } from "../../../../service/BookService";
import "../../css/BookDetail.css";
const BookDetail = () =>{
    const {bookId} = useParams();
    const navigate = useNavigate();
    const [book,setBook] = useState(null);
    const [loading,setLoading] = useState(true);

    useEffect(()=>{
        fetchBookDetails();
    },[bookId]);
    
    const fetchBookDetails = async () =>{
        try{
            const response = await getBookById(bookId);
            
            setBook(response.result);
        }catch(error){
            console.log("Error fetching book details:",error);
        }finally{
            setLoading(false);
        }
    };

    const handleAuthorClick = (authorId) =>{
        if(authorId){
            navigate(`/admin/users/detail/${authorId}`)
        }else{
            console.error("Author ID is missing")
        }
    };

    if(loading){
        return <div className="loading">Loading book details...</div>;
    }

    if(!book){
        return <div className="error-message">No book found.</div>
    }

    return (
        <div className="book-detail-container">
            <div className="book-header">
                <h1 className="book-title">{book.title}</h1>
            </div>
            <div className="book-detail-content">
                {/* Thumbnail Section */}
                <div className="book-thumbnail">
                    {book.thumbnail ? (
                        <img 
                            src={book.thumbnail}
                            alt="Book Thumbnail"
                            className="thumbnail-image"
                        />
                    ):(
                        <p>No image available</p>
                    )}
                </div>

                {/* Infomation section */}
                <div className="book-info-section">
                    <h2>Book Information</h2>
                    <p>
                        <strong>Description:</strong>  <div dangerouslySetInnerHTML={{ __html: book.description }} />
                    </p>
                    <p>
                        <strong>Author:</strong> {" "}
                        {book.authorName && book.authorId ? (
                            <span
                                className="author-link"
                                onClick={() =>handleAuthorClick(book.authorId)}
                            >
                                {book.authorName}
                            </span>
                        ):(
                            "Unknown"
                        )}
                    </p>
                    <p>
                        <strong>Price:</strong> {book.price}
                    </p>
                    <p>
                        <strong>Isbn:</strong> {book.isbn}
                    </p>
                    <p>
                        <strong>Category:</strong> {book.category}
                    </p>
                </div>
            </div>
            <div className="book-status-section">
                <h2>Status</h2>
                <p>
                    <strong>Created At:</strong> {" "}
                    {book.createdAt 
                        ?new Date(book.createdAt).toLocaleString()
                        :"N/A"}
                </p>
                <p>
                    <strong>Updated At:</strong> {" "}
                    {book.updatedAt 
                        ?new Date(book.updatedAt).toLocaleString()
                        :"N/A"}
                </p>
            </div>
        </div> 
    );
};

export default BookDetail;