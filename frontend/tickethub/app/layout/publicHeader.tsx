import { Link, NavLink } from "react-router";
import { Navbar, Nav, Container } from "react-bootstrap";
import { PublicNavMenu } from "~/components/publicNavBar/PublicNavMenu";
import { API_URL } from "~/services/homeService";
import { useStore } from "~/store/useStore";

export const PublicHeader = () => {
    const { user, isAuthenticated } = useStore();
    const isAdmin = isAuthenticated && user?.admin;
    const isClient = isAuthenticated && !isAdmin;

    return (
        <Navbar expand="lg">
            <Container fluid>
                <Navbar.Brand as={Link} to="/">
                    TicketHub
                </Navbar.Brand>

                <Navbar.Toggle aria-controls="navbarMain" />

                <Navbar.Collapse id="navbarMain">
                    <PublicNavMenu />

                    <div className="d-flex align-items-center gap-3">
                        {isAuthenticated && <>
                            {isAdmin && (
                                <Link to="/admin" className="btn btn-outline-primary btn-sm">
                                    Vista de admin
                                </Link>
                            )}

                            {isClient && <>
                                <Link to="/clients/profile">
                                    <img
                                        src={`${API_URL}/users/${user!.userID}/image`}
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
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};
