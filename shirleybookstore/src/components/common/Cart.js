import { Link } from "react-router-dom"

export const Cart = ({role,lengthCart})=>{
    return (
        role.includes("USER") && (
            <div className="nav-item mx-2">
                <Link to="/cart" className="btn btn-light rounded-circle d-flex align-items-center justify-content-center position-relative" style={{ width: '40px', height: '40px' }}>
                   <i className="fa fa-shopping-cart"></i>
                    {lengthCart >0&&(//số thư chưa đọc
                        <span className="badge bg-danger position-absolute top-0 start-100 translate-middle p-1 rounded-circle">
                            {lengthCart}
                        </span>
                    )}
                </Link>
            </div>
        )
    );
}
export default Cart;