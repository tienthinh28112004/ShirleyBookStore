export const Message = () =>{
    return (
        <div className="nav-item dropdown mx-2">
            <button className="btn btn-light rounded-circle d-flex align-items-center justify-content-center"
                style={{width:'40px',height:'40px'}} data-bs-toggle="dropdown">
                <i className="fa-solid fa-envelope"></i>
            </button>
            <ul className="dropdown-menu dropdown-menu-end">
                <li className="dropdown-item">Message from Admin</li>
                <li className="dropdown-item">New Course Alert</li>
            </ul>
        </div>
    )
}