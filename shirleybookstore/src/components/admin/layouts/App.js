import { Suspense } from "react";
import { Route, Routes } from "react-router-dom";
import Dashboard from "./Dashboard";
import UserManager from "../components/User/UserManager";
import UserDetail from "../components/User/UserDetail";
import BookManage from "../components/Book/BookManager";
import BookDetail from "../components/Book/BookDetail";
import { NotFound } from "../../error/NotFound";
import AdminLayout from "./AdminLayout";
import "../css/style.scss";
import OrderManage from "../components/Order/OrderManager";
import OrderDetail from "../components/Order/OrderDetail";
import { UploadAdminBook } from "../components/Book/BookAdminUpload";
import CatgoryManage from "../components/Category/CategoryManage";
import { UploadCategory } from "../components/Category/CategoryUpload";
import AuthorCensor from "../components/Teacher/AuthorSensor";
import AuthorInforDetail from "../components/Teacher/AuthorInfoDetail";

const AdminApp = () => {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Routes>
        {/* Các route bên trong AdminLayout */}
        <Route element={<AdminLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="users" element={<UserManager />} />
          <Route path="users/detail/:id" element={<UserDetail />} />

          {/* Thêm route này */}
          <Route path="book/manage" element={<BookManage />} />
          <Route path="book/detail/:bookId" element={<BookDetail />} />
          <Route path="book/upload-book" element={<UploadAdminBook />}/>

          <Route path="order/manage" element={<OrderManage />} />
          <Route path="order/detail/:orderId" element={<OrderDetail />} />

          <Route path="authors/censor" element={<AuthorCensor />} />
          <Route
            path="authors/detail/:id"
            element={<AuthorInforDetail />}
          />

          <Route path="category/manage" element={<CatgoryManage />} />
          <Route path="category/upload-category" element={<UploadCategory />} />
        </Route>
        {/* Route riêng cho NotFound */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Suspense>
  );
};

export default AdminApp;