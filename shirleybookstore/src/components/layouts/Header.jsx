import React, { useContext, useEffect, useRef } from "react";
import { NavLink, useLocation, useNavigate } from "react-router-dom";
import AuthContext from "../../context/AuthContext.jsx";
import { useAuthData } from "../../hooks/useAuthData.js";
import { useNotification } from "../../hooks/useNotification.js";
// import { useUserProfile } from "../../hooks/useUserProfile.js";
import { HandleLogout } from "../authentication/HandleLogout.js";
import LoadingSpinner from "../../utils/LoadingSpinner.js";
import { Advertisement } from "../common/Advertisement.js";
import { Favorites } from "../common/Favorites.js";
import { NavigationMenu } from "../common/NavigationMenu.js";
import { NotificationDropdown } from "../common/NotificationDropdown.js";
import { ProfileDropdown } from "../common/ProfileDropdown.js"
import { ViewRevenue } from "../common/ViewRevenue.js"
import { useUserProfile } from "../../hooks/useUserProfile.js";
import {Cart} from "../common/Cart.js";

export const Header = () => {
  const authContext = useContext(AuthContext);
  const location = useLocation();
  const underlineRef = useRef(null);
  const { handleLogout } = HandleLogout();
  const navigate=useNavigate();

  //Custom hook usage
  const { role, loading: authLoading } = useAuthData();
  const {
    notifications,
    unreadCount,
    markAsRead,
    loading: notificationLoading,
  } = useNotification();
  const { avatar, loading: profileLoading,lengthCart } = useUserProfile();
  console.log(lengthCart);
  console.log({role,notifications});
  //Kiểm tra trạng thái loading
  const loading = authLoading || notificationLoading || profileLoading;

  useEffect(() => {
    const activeLink = document.querySelector(`.nav-item.active`);
    if (activeLink && underlineRef.current) {
      underlineRef.current.style.left = `${activeLink.offsetLeft}px`;
      underlineRef.current.style.width = `${activeLink.offsetWidth}px`;
    }
  }, [location.pathname]);

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="header-page">
      <div className="container-fluid p-0" style={{boxShadow: "rgba(50, 50, 93, 0.25) 0px 6px 12px -2px, rgba(0, 0, 0, 0.3) 0px 3px 7px -3px"}}>
        <nav className="navbar navbar-expand-lg bg-white navbar-light py-3 py-lg-0 px-lg-5">
          <NavLink to="/" className="navbar-brand ml-lg-3" style={{cursor:"pointer"}}>
            <h1 className="m-0 text-uppercase text-primary rounded">
              <img src="https://shirley-demo.myshopify.com/cdn/shop/files/logoshirley_300x.png?v=1613554226" alt="Ảnh đại diện"/>
            </h1>
          </NavLink>

          <button
            type="button"
            className="navbar-toggler rounded"
            data-bs-toggle="collapse"
            data-bs-target="#navbarCollapse"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div
            className="collapse navbar-collapse justify-content-between px-lg-3"
            id="navbarCollapse"
          >
            <NavigationMenu
              isActive={(path) => location.pathname === path}
              underlineRef={underlineRef}
            />

            <div className="navbar-nav ml-auto d-flex align-items-center">

              <NotificationDropdown
                role={role}
                notifications={notifications}
                unreadCount={unreadCount}
                markAsRead={markAsRead}
              />
              <Cart role={role} lengthCart={lengthCart}/>
              <Favorites role={role}/>
              <ProfileDropdown
                avatar={avatar}
                isTokenValid={authContext.authenticated}
                role={role}
                handleLogout={handleLogout}
              />
            </div>
          </div>
        </nav>
      </div>
    </div>
  );
};