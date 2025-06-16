import {NotFound} from './components/error/NotFound';
import {Accessdenied} from './components/error/Accessdenied';
import { Route, Routes } from 'react-router-dom';
import 'font-awesome/css/font-awesome.min.css';
import './App.css';
import { LoginPage } from './components/pages/LoginPage/LoginPage';
import {PaymentCancel} from './components/pages/PaymentPage/PaymentCancel';
import { PaymentFail } from './components/pages/PaymentPage/PaymentFailed';
import { PaymentSuccess } from './components/pages/PaymentPage/PaymentSuccess';
import { BookDetail } from './components/pages/BookDetailPage/BookDetailPage';
import FavoriteBook from './components/pages/FavoritePage/Favorite';
import { Books } from './components/pages/BookPage/BookPage';
import { MainLayout } from './components/Router/MainLayout';
import { HeaderAndFooterRouter } from './components/Router/HeaderAndFooterRouter';
import { PrivateRoute } from './components/Router/PrivateRouter';
import { Register } from './components/pages/RegisterPage/RegisterPage';
import { RegisterAuthor } from './components/pages/RegisterPage/RegisterAuthor';
import Cart from './components/common/Cart';
import ShoppingCart from './components/pages/Cart/Cart';
import { Order } from './components/pages/OrderPage/OrderPage';
import { UploadBook } from './components/AuthorComponents/UploadBook';
import AdminApp from "./components/admin/layouts/App";
import { Profile } from './components/pages/ProfilePage/ProfilePage';
import { BookByAuthor } from './components/pages/BookByAuthorPage/BookByAuthorPage';
import { ProcessloginOAuth2 } from './components/authentication/OAuth2';
import Contact from './components/pages/ContactMe/Contact';
import ChatBox from './components/pages/ChatBox';
function App() {
  return (
    <div className="App">
      <Routes>
        {/* Admin routes */}
        <Route
          path="/admin/*"
          element={<AdminApp />}
        />
        {/* Main application routes */}
        <Route path="*" element={<NotFound />} />

        {/* Main application routes */}
        {/* <Route path="/" element={<MainLayout />}> */}
          {/* <Route index element={<HomePage />} />
          <Route path="/home" element={<HomePage />} /> */}
          {/* <Route path="/about" element={<About />} /> */}
          {/* <Route path="/contact" element={<ContactPage />} /> */}
        {/* </Route> */}
        <Route path='/' element={<MainLayout/>}>
            <Route index element={<Books/>}/>
            <Route path="/book-detail/:id" element={<BookDetail />} />
        </Route>
        <Route path="/" element={<HeaderAndFooterRouter />}>
          <Route path="/bookByAuthor" element={<BookByAuthor />} />
          {/* <Route path="/favorite" element={<FavoriteCourses />} /> */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/cart" element={<ShoppingCart />} />
          <Route path="/register" element={<Register />} />
          <Route path="/favorite" element={<FavoriteBook />} />
          <Route path="/order" element={<Order />} />
          {/* <Route
            path="/comunity"
            element={
              <PrivateRoute>
                <Community />
              </PrivateRoute>
            }
          /> */}
          {/* <Route
            path="/create-password"
            element={
              <PrivateRoute>
                <CreatePassword />
              </PrivateRoute>
            }
          /> */}
          {/* <Route
            path="/change-password"
            element={
              <PrivateRoute>
                <ChangePassword />
              </PrivateRoute>
            }
          /> */}
          <Route
            path="/profile"
            element={
              <PrivateRoute>
                <Profile />
              </PrivateRoute>
            }
          />
          {/* <Route
            path="/my-courses"
            element={
              <PrivateRoute>
                <MyCourses />
              </PrivateRoute>
            }
          /> */}
          <Route
            path="/register-teacher"
            element={
                <RegisterAuthor />
            }
          />
          {/* <Route path="/forgot-password" element={<ForgotPassword />} /> */}
          <Route
            path="/upload-book"
            element={
                <UploadBook />
            }
          />
        </Route>

        {/* <Route path="/my-account" element={<ProfilePage />} /> */}
        {/* <Route path="/detail-ads" element={<AdsDetail />} /> */}
        {/* <Route path="/courses" element={<Courses />} /> */}
        {/* <Route path="/lesson-detail/:id" element={<LearningPage />} /> */}
        <Route
          path="/oauth2/callback/:clientCode"
          element={<ProcessloginOAuth2 />}
        />
        <Route path="/accessdenied" element={<Accessdenied />} />
        <Route path="*" element={<NotFound />} />
        <Route path="/payment-success" element={<PaymentSuccess />} />
        <Route path="/payment-failed" element={<PaymentFail />} />
        <Route path="/payment-cancel" element={<PaymentCancel />} />
      </Routes>
    </div>
  );
}

export default App;
