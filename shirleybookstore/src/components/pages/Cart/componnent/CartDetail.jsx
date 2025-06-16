import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Books } from "../../BookPage/BookPage";
import { ViewBook } from "../../BookPage/components/ViewBook";
import { getAllBook } from "../../../../service/BookService";
import { motion } from "framer-motion";
export const CartDetail = ({ deleteItem, updateItem, books, clearCart,totalMoney,listBook }) => {
    const [selectedItems, setSelectedItems] = useState([]);
    const navigate = useNavigate(); // điều hướng đến trang thanh toán
    // chọn/bỏ chọn từng sản phẩm
    const handleSelectItem = (bookId) => {
        setSelectedItems((prevSelected) =>
            prevSelected.includes(bookId)
                ? prevSelected.filter((id) => id !== bookId) // khi người dùng bấm chọn nếu đã có trong selectItem thì mình bỏ chọn
                : [...prevSelected, bookId] // nếu chưa có thì mình thêm nó vào danh sách
        );
    };

    // chọn tất cả hoặc bỏ chọn tất cả
    const handleSelectAll = () => {
        if (selectedItems.length === books.length) {
            setSelectedItems([]); // xóa tất cả check box
        } else {
            setSelectedItems(books.map((book) => book.bookId)); // check box tất cả sản phẩm
        }
    };

    // Xử lý thanh toán những sản phẩm đã chọn
    const handlePayment = () => {
        const selectedBooks = books.filter((book) =>
            selectedItems.includes(book.bookId)
        );
        if(selectedBooks.length ===0){
            alert("Vui lòng chọn 1 sản phẩm để thanh toán")
        }
        const orderItems = selectedBooks.map((book) => ({
            bookId: book.bookId,
            quantity: book.quantity,
        }));
        console.log("danh sách sản phẩm được thanh toán:", orderItems);
        console.log("danh sách sản phẩm được chọn:", selectedBooks);
        // ví dụ điều hướng sang trang thanh toán và truyền dữ liệu
        navigate("/order", { state: { items: orderItems,listBooks:selectedBooks } });
    };

    const selectedTotal = books//tổng số tiền các sản phẩm đã chọn
        .filter((book) => selectedItems.includes(book.bookId))
        .reduce((sum, book) => sum + Number(book.totalPrice), 0);

    return (
         <motion.div
            initial={{opacity: 0,x: 50}}
            animate={{opacity: 1,x: 0}}
            exit={{opacity: 0, x:-50}}
            transition={{duration: 0.5}}
            className="content-page"
        >
            <div className="cart-page-main-block inner-page-sec-padding-bottom py-3 py-md-5 py-xl-8">
            <div className="cart_area cart-area-padding ">
                <div className="container">
                    <div className="page-section-title text-center" style={{paddingBottom:"35px"}}>
                        <h1>Shopping Cart</h1>
                    </div>
                    <div className="row">
                        <div className="col-12">
                            <form action="#" className="">
                                <div className="cart-table table-responsive mb--40">
                                    {books.length > 0 ? (
                                        <div>
                                            <table className="table">
                                                <thead>
                                                    <tr>
                                                        <th className="pro-remove">
                                                            <input
                                                                type="checkbox"
                                                                onChange={handleSelectAll}
                                                                checked={selectedItems.length === books.length}
                                                            />
                                                        </th>
                                                        
                                                        <th className="pro-thumbnail">Image</th>
                                                        <th className="pro-title">Product</th>
                                                        <th className="pro-price">Price</th>
                                                        <th className="pro-quantity">Quantity</th>
                                                        <th className="pro-subtotal">Total</th>
                                                        <th className="pro-remove"></th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {books.map((book) => (
                                                        <tr key={book.bookId}>
                                                            <td>
                                                                <input
                                                                    type="checkbox"
                                                                    checked={selectedItems.includes(book.bookId)}
                                                                    onChange={() => handleSelectItem(book.bookId)}
                                                                />
                                                            </td>
                                                            <td className="pro-thumbnail">
                                                                <Link to={`/book-detail/${book.bookId}`}>
                                                                    <img src={book.thumbnail} alt="Product" />
                                                                </Link>
                                                            </td>
                                                            <td className="pro-title">
                                                                <span>{book.title}</span>
                                                            </td>
                                                            <td className="pro-price">
                                                                <span>{book.priceBook}</span>
                                                            </td>
                                                            <td className="pro-quantity">
                                                                <div className="pro-qty">
                                                                    <div className="count-input-block">
                                                                        <input
                                                                            type="number"
                                                                            className="form-control text-center"
                                                                            min={1}
                                                                            defaultValue={book.quantity}
                                                                            onChange={(e) => updateItem(book.bookId, e.target.value)}
                                                                        />
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td className="pro-subtotal">
                                                                <span>{book.totalPrice}</span>
                                                            </td>
                                                            <td>
                                                                <button type="button" onClick={() => deleteItem(book.bookId)}>
                                                                    <i className="fa fa-trash-o"></i>
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </table>
                                            <div className="btn-block" style={{marginTop:"50px"}}>
                                                <button
                                                    type="button"
                                                    className="btn"
                                                    onClick={handlePayment}
                                                >
                                                    Payment
                                                </button>
                                                <Link to="/" className="btn">
                                                    Continue Shopping
                                                </Link>
                                                <button onClick={clearCart} className="btn">
                                                    Clear Cart
                                                </button>
                                            </div>
                                            <h4 style={{color:"#000", marginTop:"30px"}}>Tổng cộng ({selectedItems.length} sản phẩm): {selectedTotal} VND</h4>
                                        </div>
                                    ) : (
                                        <div>
                                            <div className="text-center pt-5 pb-5" style={{color:"#000"}}>
                                                <h3>You haven't added any items to your cart yet.</h3>
                                                <p>Keep browsing and find something you like!</p>
                                            </div>
                                        </div> 
                                    )}
                                    <div className="text-center">
                                        <hr style={{
                                            width: '50%',
                                            margin: '40px auto',
                                            border: 'none',
                                            borderTop: '3px solid #ccc'
                                        }} />
                                        <h3 style={{paddingTop:"30px",color:"#000",paddingBottom:"20px"}}>Hãy khám phá những quyển truyện mới nhất.</h3>
                                        <ViewBook books={listBook}/>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            </div>
        </motion.div>
    );
};
