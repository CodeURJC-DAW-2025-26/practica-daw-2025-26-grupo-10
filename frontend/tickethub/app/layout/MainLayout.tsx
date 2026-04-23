import { Outlet, useLocation } from "react-router";
import {AdminHeader} from "./adminHeader";
import AdminFooter from "./adminFooter";
import { PublicHeader } from "./publicHeader";
import { PublicFooter } from "./publicFooter";

const MainLayout = () => {
    const location = useLocation();
    const isAdmin = location.pathname.includes("/admin");
    return (
        <div className="d-flex flex-column min-vh-100">
            {isAdmin ? <AdminHeader /> : <PublicHeader/>}

            <main className="flex-grow-1">
                <Outlet />
            </main>

            {isAdmin ? <AdminFooter /> : <PublicFooter/>}
        </div>
    );
};

export default MainLayout;