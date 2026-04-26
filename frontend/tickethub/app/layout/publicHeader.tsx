import { Link } from "react-router";
import { PublicNavMenu } from "~/components/publicNavBar/PublicNavMenu";
import { API_URL } from "~/services/homeService";
import { useStore } from "~/store/useStore";

export const PublicHeader = () => {

    const { user, isAuthenticated } = useStore();

    const isAdmin = isAuthenticated && user?.admin;
    const isClient = isAuthenticated && !isAdmin;

    return (
        <nav className="navbar navbar-expand-lg">
            <div className="container-fluid">
                <Link className="navbar-brand" to="/">
                    TicketHub
                </Link>

                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarMain"
                    aria-controls="navbarMain"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarMain">
                    <PublicNavMenu/>

                    <div className="d-flex align-items-center gap-3">
                        {isAuthenticated && <>
                            {isAdmin && (
                                <Link to="/admin/admin" className="btn btn-outline-primary btn-sm">
                                    Vista de admin
                                </Link>
                            )}

                            {isClient && <>
                                <Link to="/clients/profile">
                                    <img
                                        src={`${API_URL}/images/users/${user?.userID}`}
                                        alt="Avatar"
                                        className="rounded-circle border border-1 border-white"
                                        style={{ width: "35px", height: "35px", objectFit: "cover" }}
                                    />
                                </Link>
                                <Link to="/clients/profile" className="btn btn-outline-primary btn-sm">
                                    Mi perfil
                                </Link>
                            </>}
                        </>}

                        {!isAuthenticated && <>
                            <Link to="/public/login" className="btn btn-outline-primary btn-sm">
                                Iniciar sesión
                            </Link>
                            <Link to="/public/signup" className="btn btn-light btn-sm">
                                Registrarse
                            </Link>
                        </>}
                    </div>
                </div>
            </div>
        </nav>
    );
};