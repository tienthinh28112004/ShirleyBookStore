import { useEffect, useState } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom";
import { createOrder } from "../../../service/OrderService";
import { createPayment } from "../../../service/PaymentService";
import { OrderForm } from "./components/OrderForm";
import { deleteItemcart } from "../../../service/CartService";

export const Order = () => {
    useEffect(() => {
        document.title = "Order Page";
    });

    const location = useLocation();
    const orderItems = location.state?.items || [];
    const listBook = location.state?.listBooks || [];
    console.log(listBook);
    //sum là tổng cộng dồn được,book là phần tử hiện tại
    const totalMoney = listBook.reduce((sum, book) => sum + book.totalPrice, 0);

    useEffect(() => {
        document.title = "Order Page";
    });

    const [formData, setFormData] = useState({
        fullName: "",
        phoneNumber: "",
        address: "",
        note: "",
        city:"", 
        district:"",
        ward:"",
        paymentExpression:"DIRECTPAYMENT",
        detailRequests: orderItems  // Sử dụng dữ liệu từ location.state
    });
    console.log(orderItems);
    console.log(formData);

    const [formErrors, setFormErrors] = useState({
        fullName: "",
        phoneNumber: "",
        address: "",
        note: "",
        city:"", 
        district:"",
        ward:"",
        paymentExpression: "",
        detailRequests: ""
    });

    const handleDeleteBookcart = async (bookId) =>{
        try{
            await deleteItemcart(bookId);
            //xóa bản ghi trùng với favoriteId và cập nhập lại danh sách favorites             
        }catch(error){
            console.error("Error delete favorite:",error);
        }
    };
    
    const [errorMessage, setErrorMessage] = useState("");//Hiển thị thông báo lỗi
    const navigate = useNavigate();

    //Xử lý thay đổi giá trị các input
    const handleInputChange = (e) => {
        const { name, value } = e.target;

        setFormData({
            ...formData,
            [name]: value,
        });

        setFormErrors({
            ...formErrors,
            [name]: value ? "" : formErrors[name],
        });
    }

    //Xử lý lỗi khi để trống
    const handleInputBlur = (event) => {
        const { name, value } = event.target;
        if (!value) {
            setFormErrors({
                ...formErrors,
                [name]: "This field cannot be left blank"
            });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // console.log(formData);
            const data = await createOrder(formData.fullName, formData.phoneNumber, formData.address, formData.note, formData.paymentExpression, formData.detailRequests);
            console.log(data);
            if(data.result){
                if (data.result.paymentExpression === "ONLINEPAYMENT") {
                    const vnpayUrl = await createPayment(data.result.totalMoney||listBook[0].price*orderItems[0].quantity);
                    console.log(vnpayUrl.result.paymentUrl);
                    window.location.href = vnpayUrl.result.paymentUrl;
                }else{
                    navigate("/payment-success");
                }
                formData.detailRequests.map((detail)=>
                    handleDeleteBookcart(detail.bookId)
            )
            }
            

            //alert("Thanh toán thành công,đơn hàng sẽ được giao đến bạn trong vài ngày tới!");
        } catch (err) {
            console.error(err);
            setErrorMessage("Các trường không hợp lệ");
        }
    }

    return (
            <div className="cart-page-main-block inner-page-sec-padding-bottom py-3 py-md-5 py-xl-8">
                <div className="cart_area cart-area-padding " style={{ marginTop: "100px" }}>
                    <div className="container">
                        <div className="page-section-title vip-title">
                            <h1> Your order</h1>
                        </div>
                        <OrderForm 
                            handleSubmit={handleSubmit}
                            handleInputBlur={handleInputBlur}
                            handleInputChange={handleInputChange}
                            errorMessage={errorMessage}
                            formData={formData}
                            formErrors={formErrors}
                            setFormData={setFormData}
                        />
                        <div className="row">
                            <div className="col-12">
                                <form action="#" className="">
                                    <div className="cart-table table-responsive mb--40">
                                        <div>
                                            <table className="table">
                                                <thead>
                                                    <tr>
                                                        <th className="pro-thumbnail">Image</th>
                                                        <th className="pro-title">Product</th>
                                                        <th className="pro-price">Price</th>
                                                        <th className="pro-quantity">Quantity</th>
                                                        <th className="pro-subtotal">Total</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {listBook.map((book) => (
                                                        <tr key={book.bookId}>
                                                            <td className="pro-thumbnail">
                                                                <Link to={`/book-detail/${book.bookId}`}>
                                                                    <img src={book.thumbnail} alt="Product" />
                                                                </Link>
                                                            </td>
                                                            <td className="pro-title">
                                                                <span>{book.title}</span>
                                                            </td>
                                                            <td className="pro-price">
                                                                <span>{book.priceBook||book.price}</span>
                                                            </td>
                                                            <td className="pro-quantity">
                                                                <div className="pro-qty">
                                                                    {book.quantity||orderItems[0].quantity}
                                                                </div>
                                                            </td>
                                                            <td className="pro-subtotal">
                                                                <span>{book.totalPrice||book.price*orderItems[0].quantity}</span>
                                                            </td>
                                                        </tr>
                                                    ))}
                                                </tbody>
                                            </table>
                                            <h4 style={{color:"#000", marginTop:"20px"}}>Tổng cộng ({orderItems.length} sản phẩm): {totalMoney||listBook[0].price*orderItems[0].quantity} VND</h4>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    );
}
