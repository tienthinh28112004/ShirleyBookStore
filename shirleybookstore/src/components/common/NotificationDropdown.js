export const NotificationDropdown = ({role,notifications=[],unreadCount=0,markAsRead}) =>{
    console.log(role);
    console.log(notifications);
    return (
        <>
            {role.length >0 && (
                <div className="nav-item dropdown mx-2 position-relative">
                    <button
                        className="btn btn-light rounded-circle d-flex align-items-center justify-content-center position-relative"
                        style={{width:'40px',height: '40px'}}
                        data-bs-toggle="dropdown"
                    >
                        <i className="fa fa-bell"></i>
                        {unreadCount >0&&(//số thư chưa đọc
                            <span className="badge bg-danger position-absolute top-0 start-100 translate-middle p-1 rounded-circle">
                                {unreadCount}
                            </span>
                        )}
                    </button>
                    <ul className="dropdown-menu dropdown-menu-end p-3 notifications">
                        {notifications.length === 0?(
                            <li className="dropdown-item text-center text-muted">No new notifications</li>
                        ):(
                            notifications.map((notification)=>(
                                <li key={notification.id}
                                    className={`notification-item ${notification.isRead ? 'read':'unread'}`}
                                    style={{padding:'10px 0',borderBottom: '1px solid #ddd',width:"250px"}}
                                >
                                    <img 
                                        src={notification.avatarUrl || "https://bootdey.com/img/Content/avatar/avatar7.png"}
                                        alt="Sender Avatar"
                                        className="rounded-circle me-3"
                                        style={{width:'45px',height:'45px'}}
                                    />
                                    <div>
                                        <h6 style={{fontWeight:notification.isRead ? 'normal' : 'bold'}}>
                                            {notification.title}
                                        </h6>
                                        <small className="text-muted d-block mb-1">{notification.messages}</small>
                                        <small className="text-muted d-block mb-1">{notification.elapsed}</small>
                                        <button
                                            className="btn btn-sm btn-link"
                                            onClick={()=>markAsRead(notification.id)}
                                        >
                                            <i className="fa fa-check-circle"></i>Mark as read
                                        </button>
                                    </div>
                                </li>
                            ))
                        )}
                    </ul>
                </div>
            )}
        </>
    );
};