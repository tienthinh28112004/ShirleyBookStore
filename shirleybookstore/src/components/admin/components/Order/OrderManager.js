/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import { TablePagination } from "@mui/material";
import { changeStatus, getListOrders } from "../../../../service/OrderService"
import "../../css/OrderManage.css"


const OrderManage = ()=>{
    const [orders,setOrders] = useState([]);//Lưu danh sách order
    const [page,setPage] = useState(0);//Trang hiện tại
    const [rowsPerPage,setRowsPerPage]= useState(7);//Số dòng mỗi trang
    const [totalItems,setTotalItems] = useState(0);//Tổng số khóa học
    const navigate = useNavigate();//Hook điều hướng
    const [orderStatus,setOrderStatus]=useState("PENDDING");
    const statusOptions = [
        { value: "PENDDING", label: "Đang xác nhận đơn hàng" },
        { value: "CONFIRMED", label: "Xác nhận đơn hàng" },
        { value: "PROCESSING", label: "Đang vận chuyển" },
        { value: "CANCELED", label: "Giao hàng thất bại" },
        { value: "COMPLETE", label: "Giao hàng thành công" },
      ];
      
    useEffect(() => {
        fetchOrders();
    },[page,rowsPerPage]);

    const fetchOrders = async() =>{
        try{
            console.log(1)
            const response= await getListOrders(page+1,rowsPerPage);
            console.log(response)
            setOrders(response.result.items);
            setTotalItems(response.totalElements);
        }catch(error){
            console.error("Error fetching orders:",error)
        }
    };
    
    const changeStatusOrder = async(orderId,orderStatus) =>{
        try{
            await changeStatus(orderId,orderStatus);

            setOrders((prevOrders) =>
                prevOrders.map((order) =>
                  order.orderId === orderId ? { ...order, orderStatus: orderStatus } : order
                )
              );
        }catch(error){
            console.log("Error status order",error);
        }
    }

    //Điều hướng đến trang chi tiết khóa học
    const handleRowClick = (id) =>{
        navigate(`/admin/order/detail/${id}`);//chuyển hướng tới đúng URL
    }

    return (
        <div className="order-manage">
            <h2 className="order-manage-title">Order Management</h2>
            <div className="order-manage-table">
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th className="table-icon">fullName</th>
                            <th className="table-icon">PhoneNumber</th>
                            <th className="table-icon">Address</th>
                            <th className="table-icon">Created At</th>
                            <th className="table-icon">Payment Expression</th>
                            <th className="table-icon">Order Status</th>
                            <th className="table-icon">Total Money</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map((order,index) =>(
                            <tr key={order.id}>
                                <td>{page*rowsPerPage+index+1}</td>
                                <td 
                                    onClick={() => handleRowClick(order.orderId)}
                                    style={{cursor:"pointer"}}
                                >{order.fullName}</td>
                                <td
                                    onClick={() => handleRowClick(order.orderId)}
                                    style={{cursor:"pointer"}}
                                >{order.phoneNumber || "Unknown"}</td>
                                <td
                                    onClick={() => handleRowClick(order.orderId)}
                                    style={{cursor:"pointer"}}
                                >{order.address || "Unknown"}</td>
                                <td>
                                    {order.createdAt 
                                        ?new Date(order.createdAt).toLocaleString()
                                        :"N/A"}
                                </td>
                                <td>{order.paymentExpression || "Unknown"}</td>
                                <td>
                                <select 
                                    value={order.orderStatus}
                                    onChange={(e) => changeStatusOrder(order.orderId, e.target.value)}
                                >
                                    {statusOptions.map((option) => (
                                        <option key={option.value} value={option.value}>
                                            {option.label}
                                        </option>
                                    ))}
                                </select>
                                   
                                </td>
                                <td>{order.totalMoney} VND</td>
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

export default OrderManage;