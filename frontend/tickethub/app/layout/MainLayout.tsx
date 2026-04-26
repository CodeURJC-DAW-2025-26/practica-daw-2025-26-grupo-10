import { useEffect } from "react";
import { Navigate, Outlet, useLocation, useNavigation } from "react-router";
import {AdminHeader} from "./adminHeader";
import AdminFooter from "./adminFooter";
import GlobalSpinner from "~/components/GlobalSpinner";
import { PublicHeader } from "./publicHeader";
import { PublicFooter } from "./publicFooter";
import { useStore } from "~/store/useStore";

const MainLayout = () => {
    const location = useLocation();
    const navigation = useNavigation();
    const { user, isAuthenticated, isInitialized, initialize } = useStore();
    const isAdmin = location.pathname.startsWith("/admin");
    const isNavigating = navigation.state === "loading";

    useEffect(() => {
        initialize();
    }, []);

    if (!isInitialized) {
        return <GlobalSpinner />;
    }

    if (isAdmin && (!isAuthenticated || !user?.admin)) {
        return <Navigate to="/403" replace />;
    }

    return (
        <div className="d-flex flex-column min-vh-100">
            {isAdmin ? <AdminHeader /> : <PublicHeader />}

            <main className="flex-fill">
                {isNavigating && <GlobalSpinner />}

                <Outlet />
            </main>

            {isAdmin ? <AdminFooter /> : <PublicFooter />}
        </div>
    );
};

export default MainLayout;