import { Link, useNavigate } from "react-router";
import { Button } from "react-bootstrap";
import { useStore } from "../../store/useStore";
import { useState } from "react";

export const AdminNavActions = () => {
    const logout = useStore((state) => state.logout);
    const navigate = useNavigate();
    const [isLoggingOut, setIsLoggingOut] = useState(false);

    const handleLogout = async () => {
        if (isLoggingOut) return;
        setIsLoggingOut(true);
        try {
            await logout();
            navigate("/");
        } finally {
            console.error("Error al cerrar sesión");
            setIsLoggingOut(false);
        }
    };

    return (
        <div className="d-flex gap-2">
            <Link to="/" className="btn btn-outline-primary btn-sm">
                Vista de cliente
            </Link>
            <Button variant="danger" onClick={handleLogout} disabled={isLoggingOut}>
                Cerrar sesión
            </Button>
        </div>
    );
};
