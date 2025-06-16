import { Link } from "react-router-dom"

export const Favorites = ({role}) =>{
    // if (loading || !role) return null;
    return (
        
            role.includes("USER") && (
                <div className="nav-item mx-2">
                    <Link to="/favorite" className="btn btn-light rounded-circle d-flex align-items-center justify-content-center" style={{ width: '40px', height: '40px' }}>
                        <i className="fa fa-heart"></i>
                    </Link>
                </div>
            )
    
    )
}