import { Link, useNavigate } from "react-router";
import { useStore } from "../../store/useStore";

export const AdminNavActions = () => {
    const logout = useStore((state) => state.logout);
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();//esto puede ser inseguro contra doble click, si nos sobra tiempo hay que apañarlo
        //(no se me ocurre una buena manera ahora mismo...tal vez lo mejor sea un await entre clicks y a correr)
        navigate("/");
    };

    return (
        <div className="d-flex gap-2">
            <Link to="/" className="btn btn-outline-primary btn-sm">
                Vista de cliente
            </Link>
            <button onClick={handleLogout} className="btn btn-danger">
                Cerrar sesión
            </button>
        </div>
    );
};