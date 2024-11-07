import { Outlet } from "react-router-dom";
import BackgroundImageSlider from "../common/BackgroundImageSlider";
import NavBar from "./NavBar";
const RootLayout = () => {
    console.log("Root layout");
    return (
        <main>
            <NavBar></NavBar>
            <BackgroundImageSlider></BackgroundImageSlider>
            <div>
                <Outlet></Outlet>
            </div>
        </main>
    );
};

export default RootLayout;
