import { CNavGroup, CNavItem, CNavTitle, CSidebarNav } from "@coreui/react";
import { useState } from "react"
import { NavLink } from "react-router-dom";
import "../css/AdminSidebarNav.css";
import { cilPeople } from "@coreui/icons";
import CIcon from "@coreui/icons-react";
const AppSideBarNav = () =>{
    const [openItems,setOpenItems] = useState({
        user:false,
        order:false,
        author: false,
        book:false,
        category:false
    });

    const toggleItem = (name) =>{
        setOpenItems((prevOpenItems) =>({
            ...prevOpenItems,
            [name]:!prevOpenItems[name],
        }));
    };

    const renderNavItem = (text,link) =>(
        <CNavItem>
            <NavLink to={link} className="nav-link-custom">
                <span className="nav-bullet">-</span> {text}
            </NavLink>
        </CNavItem>
    );

    return (
        <CSidebarNav>
            {/* MANAGEMENT Group */}
            <CNavTitle style={{color:"#fff"}}>MANAGEMENT</CNavTitle>
            <CNavGroup
                toggler={<div className="me-2">User</div>}
                onClick={()=>toggleItem("user")}
                visible={openItems["user"]}//dùng để hiển thị true/flase của biến user
            >
                {renderNavItem("Manage","/admin/users")}
            </CNavGroup>
            <CNavGroup
                toggler={<div className="me-2">Book</div>}
                onClick={()=>toggleItem("book")}
                visible={openItems["book"]}
            >
                {renderNavItem("Manage","/admin/book/manage")}
                {renderNavItem("Upload Book","/admin/book/upload-book")}
            </CNavGroup>
            <CNavGroup
                toggler={<div className="me-2">Order</div>}
                onClick={()=>toggleItem("order")}
                visible={openItems["order"]}
            >
                {renderNavItem("Manage","/admin/order/manage")}
            </CNavGroup>
            <CNavGroup
                toggler={
                <>
                    <CIcon icon={cilPeople} className="me-2" />
                    Author
                </>
                }
                onClick={() => toggleItem("author")}
                visible={openItems["author"]}
            >
                {renderNavItem("Censor", "/admin/authors/censor")}
            </CNavGroup>
            <CNavGroup
                toggler={<div className="me-2">Category</div>}
                onClick={()=>toggleItem("category")}
                visible={openItems["category"]}//dùng để hiển thị true/flase của biến user
            >
                {renderNavItem("Manage","/admin/category/manage")}
                {renderNavItem("Create Category","/admin/category/upload-category")}
            </CNavGroup>
            {/* ANALYSIS Group */}
            <CNavTitle style={{color:"#fff"}}>ANALYSIS</CNavTitle>
        </CSidebarNav>
    )
}

export default AppSideBarNav;