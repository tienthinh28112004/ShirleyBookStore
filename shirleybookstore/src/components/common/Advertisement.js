import { Link } from "react-router-dom"

//dùng hay không dùng thì cứ liệt kê vào
export const Advertisement= () =>{
    return (
        <div className="nav-item mx-2">
            <Link to="/my-ads">
                <button 
                    className="btn btn-light rounded-circle d-flex align-items-center justify-content-center"
                    style={{width: '40px',height: '40px'}}
                >
                <i className="fa fa-newspaper-o"></i>
                </button>
            </Link>
        </div>
    )
}