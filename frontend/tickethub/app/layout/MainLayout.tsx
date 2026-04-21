import { Outlet } from "react-router";
import {AdminHeader} from "./adminHeader";
import AdminFooter from "./adminFooter";

const MainLayout = () => {
    return (
        <div className="d-flex flex-column min-vh-100">
            <AdminHeader />

            <main className="flex-grow-1">
                <Outlet />
            </main>

            <AdminFooter />
        </div>
    );
};

export default MainLayout;