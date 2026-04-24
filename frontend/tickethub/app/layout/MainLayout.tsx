import { Outlet, useLocation, useNavigation } from "react-router";
import {AdminHeader} from "./adminHeader";
import AdminFooter from "./adminFooter";
import GlobalSpinner from "~/components/GlobalSpinner";

const MainLayout = () => {
    const location = useLocation();
    const navigation = useNavigation();
    const isAdmin = location.pathname.startsWith("/admin");
    const isLoading = navigation.state === "loading";
    
    return (
        <>
            {isAdmin ? <AdminHeader /> : <PublicHeader />}

            {isLoading && <GlobalSpinner />}

            <Outlet />

            {isAdmin ? <AdminFooter /> : <PublicFooter />}
        </>
    );
};

export default MainLayout;