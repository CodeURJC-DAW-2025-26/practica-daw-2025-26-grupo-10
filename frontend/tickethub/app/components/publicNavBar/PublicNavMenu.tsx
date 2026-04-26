import { NavLink } from "react-router";
import { Nav } from "react-bootstrap";

export const PublicNavMenu = () => {
    return (
        <Nav className="me-auto mb-2 mb-lg-0">
            <Nav.Link as={NavLink as any} to="/public/events">Eventos</Nav.Link>
            <Nav.Link as={NavLink as any} to="/public/artists">Artistas</Nav.Link>
        </Nav>
    );
};
