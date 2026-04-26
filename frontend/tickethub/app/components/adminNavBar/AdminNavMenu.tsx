import { NavLink } from "react-router";
import { Nav } from "react-bootstrap";

export const AdminNavMenu = () => {
    return (
        <Nav className="me-auto">
            <Nav.Link as={NavLink as any} to="/admin/events">Gestionar Eventos</Nav.Link>
            <Nav.Link as={NavLink as any} to="/admin/artists/manage_artists">Gestionar Artistas</Nav.Link>
            <Nav.Link as={NavLink as any} to="/admin/statistics">Estadísticas</Nav.Link>
        </Nav>
    );
};
