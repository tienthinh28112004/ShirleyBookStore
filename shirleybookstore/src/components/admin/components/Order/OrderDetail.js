
import {useNavigate, useParams} from "react-router-dom";
import { getOrderById } from "../../../../service/OrderService";
import { useEffect, useState } from "react";


const OrderDetail = () =>{
    const [orderDetails,setOrderDetails] = useState(null);
    const {orderId} = useParams();
    const navigate = useNavigate();

    useEffect(() =>{
        const fetchDetails = async () =>{
            try{
                const data = await getOrderById(orderId);
                setOrderDetails(data.result);
            }catch(error){
                console.log(error);
            }
        };
        fetchDetails();
    },[orderId]);

    if(!orderDetails){
        return <div>Loading...</div>
    }

    return (
        <div className="user-detail">
            <div className="detail-section">
                <div className="info-card">
                    <h3>fullName</h3>
                    <p>{orderDetails.fullName}</p>
                </div>
                <div className="info-card">
                    <h3>phoneNumber</h3>
                    <p>{orderDetails.phoneNumber}</p>
                </div>
                <div className="info-card">
                    <h3>email</h3>
                    <p>{orderDetails.email}</p>
                </div>
                <div className="info-card">
                    <h3>note</h3>
                    <p>{orderDetails.note}</p>
                </div>
                <div className="info-card">
                    <h3>dateTime</h3>
                    <p>{orderDetails.dataTime}</p>
                </div>
                <div className="info-card">
                    <h3>OrderStatus</h3>
                    <p>{orderDetails.OrderStatus}</p>
                </div>
                <div className="info-card">
                    <h3>Payment Expression</h3>
                    <p>{orderDetails.paymentExpression}</p>
                </div>
                <div className="info-card">
                    <h3>Total money</h3>
                    <p>{orderDetails.totalMoney}</p>
                </div>
            </div>
        </div>
    )
}

export default OrderDetail;