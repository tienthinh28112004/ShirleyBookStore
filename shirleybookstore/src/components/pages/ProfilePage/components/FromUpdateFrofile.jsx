import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export const FormUpdateProfile = (props) => {
    const navigate=useNavigate();
    const {
        handleUpdateProfile,
        selectedImage,
        handleOnChangeAvatar,
        handleUpdateAvatar,
        isUpdatingAvatar,
        isRemovingAvatar,
        handleRemoveAvatar,
        handleInputChange,
        profileData,
        orders,
        addItemCarts
    } = props;

    const handleAddAllToCart = (order) => {
        order.detailResponse.forEach(item => {
            addItemCarts(item.bookId, item.quantity);
        });
        navigate(`/cart`)
    };
    const [showProfile, setShowProfile] = useState(true);
    const handleDetailBook =(id) =>{
        navigate(`/book-detail/${id}`);//điều hướng đến trang chi tiết người dùng
    }

    return (
        <div className="container">
            <div className="row gutters">
                {/* Cột trái */}
                <div className="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
                    <div className="card h-100">
                        <div className="card-body">
                            <label
                                htmlFor="avatar"
                                className="fw-bold fs-6 text-dark text-center d-flex justify-content-center"
                            >
                                Ảnh đại diện
                            </label>
                            <div className="mb-3 d-flex justify-content-center">
                                <img
                                    src={selectedImage}
                                    alt="User Avatar"
                                    className="rounded-circle"
                                    style={{
                                        width: "150px",
                                        height: "150px",
                                        objectFit: "cover",
                                        border: "3px solid #62ab00",
                                        cursor: "pointer",
                                    }}
                                    onClick={() =>
                                        document.getElementById("url-update-avatar").click()
                                    }
                                />
                            </div>
                            <input
                                type="file"
                                hidden
                                id="url-update-avatar"
                                name="file"
                                accept="image/*"
                                onChange={handleOnChangeAvatar}
                            />
                            <div className="d-flex justify-content-center gap-2 mt-2">
                                {isUpdatingAvatar ? (
                                    <button className="btn btn-primary btn-sm" disabled>
                                        <i className="fas fa-spinner fa-spin me-1"></i> Updating...
                                    </button>
                                ) : (
                                    <button
                                        className="btn btn-primary btn-sm"
                                        onClick={handleUpdateAvatar}
                                    >
                                        <i className="fas fa-upload me-1"></i> Update
                                    </button>
                                )}
                                {isRemovingAvatar ? (
                                    <button className="btn btn-danger btn-sm" disabled>
                                        <i className="fas fa-spinner fa-spin me-1"></i> Removing...
                                    </button>
                                ) : (
                                    <button
                                        className="btn btn-danger btn-sm"
                                        onClick={handleRemoveAvatar}
                                    >
                                        <i className="fas fa-trash-alt me-1"></i> Remove
                                    </button>
                                )}
                            </div>

                            <div className="btn-group mt-4 w-100" role="group">
                                <button
                                    className={`btn btn-outline-primary ${showProfile ? "active" : ""}`}
                                    onClick={() => setShowProfile(true)}
                                >
                                    Thông tin
                                </button>
                                <button
                                    className={`btn btn-outline-secondary ${!showProfile ? "active" : ""}`}
                                    onClick={() => setShowProfile(false)}
                                >
                                    Đơn hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Cột phải */}
                <div className="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">
                    <div className="card h-100">
                        <div className="card-body">
                            {showProfile ? (
                                <div className="row gutters">
                                    <div className="col-md-6 mb-3">
                                        <label className="fw-bold">Email</label>
                                        <div className="form-control">{profileData.email}</div>
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <label className="fw-bold">Họ tên</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            name="fullName"
                                            value={profileData.fullName}
                                            onChange={handleInputChange}
                                        />
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <label className="fw-bold">Số điện thoại</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            name="phoneNumber"
                                            value={profileData.phoneNumber}
                                            onChange={handleInputChange}
                                        />
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <label className="fw-bold">Ngày sinh</label>
                                        <input
                                            type="date"
                                            className="form-control"
                                            name="dob"
                                            value={profileData.dob}
                                            onChange={handleInputChange}
                                        />
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <label className="fw-bold">Ngày tạo</label>
                                        <div className="form-control">{profileData.createdAt}</div>
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <label className="fw-bold">Vai trò</label>
                                        <div className="form-control">
                                            {Array.isArray(profileData.role)
                                                ? profileData.role.join(", ")
                                                : profileData.role}
                                        </div>
                                    </div>
                                    <div className="col-12 text-end mt-3">
                                        <button
                                            className="btn btn-primary"
                                            onClick={handleUpdateProfile}
                                        >
                                            Cập nhật
                                        </button>
                                    </div>
                                </div>
                            ) : (
                                <>
                                    {orders && orders.length > 0 ? (
                                        orders.map((order) => (
                                            <div className="card mb-4 shadow-sm border" key={order.orderId}>
                                                <div className="card-header d-flex justify-content-between align-items-center py-3">
                                                    <div>
                                                        <span className="fw-bold fs-5">
                                                           Mẫ đơn hàng #{order.orderId}
                                                        </span>
                                                    </div>
                                                    <div className="d-flex align-items-center">
                                                        <span>
                                                            Trạng thái đơn hàng: <strong style={{color:"#62ab00"}}>{order.orderStatus}</strong>
                                                        </span>
                                                    </div>
                                                </div>
                                                <div className="card-header  align-items-center py-3">
                                                    <div className="text-muted small mt-1">
                                                        <strong style={{color:"#62ab00"}}>Chú ý:</strong> {order.note || "Mặc định"}
                                                    </div>
                                                    <div className="text-muted small mt-1">
                                                        <strong style={{color:"#62ab00"}}>Địa chỉ:</strong> {order.address || "Mặc định"}
                                                    </div>
                                                </div>
                                                
                                                <div className="card-body">
                                                    {order.detailResponse.map((item, index) => (
                                                        <div
                                                            onClick={()=>handleDetailBook(item.bookId)}
                                                            key={index}
                                                            style={{cursor:"pointer"}}
                                                            className="d-flex py-2 border-bottom"
                                                        >
                                                            <div className="me-3">
                                                                <img
                                                                    src={item.thumbnail}
                                                                    alt={item.title}
                                                                    width="70"
                                                                    height="70"
                                                                    className="rounded object-fit-cover"
                                                                />
                                                            </div>
                                                            <div className="flex-grow-1">
                                                                <div className="fw-medium">Tên sách: {item.title}</div>
                                                                <div className="d-flex justify-content-between mt-2">
                                                                    <span>Số Lượng: {item.quantity}</span>
                                                                    <span>Giá: {item.priceBook} VND</span>
                                                                    {/* <span className="text-primary">
                                                                        {item.priceBook.toLocaleString()}₫
                                                                    </span> */}
                                                                </div>
                                                            </div>
                                                        </div>
                                                    ))}
                                                    <div className="d-flex justify-content-between align-items-center mt-3 pt-2">
                                                        <div>
                                                            <div className="btn" onClick={() => handleAddAllToCart(order)}>
                                                                <i className="fa fa-shopping-cart me-2"></i>Mua lại
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <span className="text-muted me-2">Thành tiền:</span>
                                                            <span className="fw-bold text-danger fs-5">
                                                                {order.totalMoney.toLocaleString()}₫
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        ))
                                    ) : (
                                        <div className="text-center pt-5">
                                            <h4>Your shopping journey awaits.</h4>
                                            <p>Discover amazing products and add something special to your collection!</p>
                                            <div className="btn-block">
                                                <Link to="/cart" className="btn">Shop Now</Link>
                                            </div>
                                        </div>
                                    )}
                                </>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
