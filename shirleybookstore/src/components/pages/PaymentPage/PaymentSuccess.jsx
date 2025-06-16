import { useEffect } from "react"
import { Link } from "react-router-dom"

export const PaymentSuccess = ()=>{
    useEffect(()=>{
        document.title = 'Payment Successfully'
    })

    return (
        <div className="payment-container">
            <img 
                src="http://www.nbk.vn/upload/khoungdung/vn-pay.jpg"
                alt="VNPay"
                className="payment-vnpay-logo"
            />
            <h1>Thanh toán thành công!</h1>
            <div className="payment-checkmark"></div>
            <p>Cảm ơn bạn đã sử dụng VNPay. Giao dịch của bạn đã được xử lý thành công.</p>
            <Link to="/" className="payment-button">Quay lại trang chủ</Link>
        </div>
    )
}