import { Link } from "react-router";
import { AdminNavMenu } from "../components/adminNavBar/AdminNavMenu";
import { AdminNavActions } from "../components/adminNavBar/AdminNavActions";

export const AdminHeader = () => {
    return (
        <nav className="navbar navbar-expand-lg">
            <div className="container-fluid">

                <Link className="navbar-brand" to="/admin/admin">
                    TicketHub Admin
                </Link>

                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#adminNavbarContent"
                    aria-controls="adminNavbarContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                    style={{ borderColor: "var(--vivid-tangerine)" }}
                >
                    <span
                        className="navbar-toggler-icon"
                        style={{ filter: "invert(1)" }}
                    />
                </button>

                <div
                    className="collapse navbar-collapse"
                    id="adminNavbarContent"
                >
                    <AdminNavMenu />
                    <AdminNavActions />
                </div>

            </div>
        </nav>
    );
};