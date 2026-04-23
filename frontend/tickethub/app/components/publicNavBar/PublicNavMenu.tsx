import { Link } from "react-router"

export const PublicNavMenu = () => {
    return (
        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
                <Link className="nav-link" to="/public/events">Eventos</Link>
            </li>
            <li className="nav-item">
                <Link className="nav-link" to="/public/artists">Artistas</Link>
            </li>
        </ul>
    )
}