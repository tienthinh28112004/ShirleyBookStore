/* eslint-disable jsx-a11y/alt-text */
import { Link } from "react-router-dom"

export const ViewBook = ({books}) =>{
    return (
        <div className="row" style={{width:"100%"}}>
            {books.length> 0?
                books.map((book) =>(
                    <div className="col-lg-2 col-md-4 col-sm-6 pb-4" key={book.id}>
                        <Link className="books-list-item" to={`/book-detail/${book.id}`}>
                            <img className="img-fluid" src={book.thumbnail} alt="Book Thumbnail" />
                            <div className="books-info">
                                <div className="books-author">
                                    <span><i className="fa fa-user mr-2"></i>{book.authorName}</span>
                                </div>
                                <div className="books-title">{book.title}</div>
                                <div>Thể loại: {book.category}</div>
                                <div className="book-price mt-2">
                                    <strong>Price: </strong>
                                    <span className="book-price-value">{book.price} VND</span>
                                </div>
                            </div>
                        </Link>
                    </div>
                ))
            :(
                <div className="text-center pt-5">
                    <h4>We couldn't find any books that match your search.</h4> 
                    <p>Try adjusting your filters or explore different categories!"</p>
                </div>
            )}
        </div>
    )
}