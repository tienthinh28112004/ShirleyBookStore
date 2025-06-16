import { Outlet } from "react-router-dom"
import AdminHeader from "../components/AdminHeader"
import AdminSidebar from "../components/AdminSidebar"
import AdminFooter from "../components/AdminFooter"

const AdminLayout = () =>{
    return(
        <div className="admin-app" style={{paddingTop:"0"}}>
            <AdminSidebar />
            <div className="wrapper d-flex flex-column min-vh-100">
                <AdminHeader />
                <div className="body flex-grow-1">
                    <Outlet /> {/*Đây là nơi các phần con được hiển thị*/ }
                </div>
                <AdminFooter />
            </div>
        </div>
    );
};

export default AdminLayout;