import { Outlet } from "react-router-dom";
import { Header } from "../layouts/Header";
import { Footer } from "../layouts/Footer";
import { TopBar } from "../layouts/TopBar";
import HeroSlider from "../layouts/HeroCarosel";
import ChatBox from "../pages/ChatBox";

export const HeaderAndFooterRouter = () => {
    return(
        <div>
            <TopBar />
            <Header/>
            <Outlet />


            <Footer/>
             <ChatBox />
        </div>
    );
}