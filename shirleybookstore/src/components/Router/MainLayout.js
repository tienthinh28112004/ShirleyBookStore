import React from "react";
import { Outlet } from "react-router-dom";
import { Footer } from "../layouts/Footer";
import { Header } from "../layouts/Header";
import { TopBar } from "../layouts/TopBar";
import {HeroSlider} from "../layouts/HeroCarosel";
import ChatBox from "../pages/ChatBox";

export const MainLayout = () => {
    return (
        <div>
            <TopBar />
            <Header/>
            <HeroSlider />
            {/* Outlet sẽ render các thành phần con, nội dung tương ứng với route */}
            <Outlet />

            <Footer />
             <ChatBox />
        </div>
    );
}