import {Spinner} from "react-bootstrap";
import { FaTrashAlt } from "react-icons/fa";
import { MdOutlineDescription } from "react-icons/md";
import { Link } from "react-router-dom";
export const ViewFavorite = (props) =>{
    const {loading,favorites ,handleDeleteFavorite} = props;
    console.log(favorites);
    return(
        <div className="row">
            {loading ? (
                <div className="col-12 text-center">
                    <Spinner>
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </div>
            ):favorites && favorites.length > 0 ?(
                favorites.map((favorite) => (
                    <div className="col-lg-2 col-md-4 col-sm-6 pb-4" key={favorite.favoriteId}>
                        <div className="book-list-item">
                            <Link to={`/book-detail/${favorite.bookId}`}>
                                <img 
                                    className="img-fluid"
                                    src={favorite.thumbnail}
                                    alt="Book Thumbnail"
                                    style={{ width: '100%', height: '200px', objectFit: 'cover' }}
                                />
                            </Link>
                            <div className="book-info">
                                <div className="book-author">
                                    <span>
                                        <i className="fa fa-user mr-2"></i>
                                        {favorite.author || "Unknown Author"}
                                    </span>
                                </div>
                                <div>
                                    <span>{favorite.title}</span>
                                </div>
                                <div className="book-meta">
                                    <span>
                                        <i className="fa fa-star mr-2"></i> 4.5 (250)
                                    </span>
                                </div>
                                <div className="book-price mt-2">

                                    <span className="book-price-value">
                                        Price: {favorite.priceBook} {" VND"}
                                    </span>
                                </div>
                                <div className="d-flex justify-content-between mt-3">
                                    <Link
                                        to={`/book-detail/${favorite.bookId}`}
                                        className="btn btn-outline-primary btn-sm"
                                    >
                                        <MdOutlineDescription className="mr-2"/>
                                        Detail
                                    </Link>
                                    <button
                                        className="btn btn-outline-danger btn-sm"
                                        onClick={() => handleDeleteFavorite(favorite.favoriteId)}
                                    >
                                        <FaTrashAlt className="mr-2"/>
                                        Remove
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                ))
            ) : (
                <div className="text-center pt-5">
                    <h4>You haven't marked any books as favorites yet.</h4>
                    <p>Explore and find your next favorite read!</p>
                </div>
            )}
        </div>
    );
};