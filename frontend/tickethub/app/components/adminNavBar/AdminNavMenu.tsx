import { NavLink } from "react-router";

export const AdminNavMenu = () => {
    return (
        <ul className="navbar-nav me-auto">
            <li className="nav-item">
                <NavLink className="nav-link" to="/admin/events">
                    Gestionar Eventos
                </NavLink>
            </li>
            <li className="nav-item">
                <NavLink className="nav-link" to="/admin/artists/manage_artists">
                    Gestionar Artistas
                </NavLink>
            </li>
            <li className="nav-item">
                <NavLink className="nav-link" to="/admin/statistics">
                    Estadísticas
                </NavLink>
            </li>
        </ul>
    );
};