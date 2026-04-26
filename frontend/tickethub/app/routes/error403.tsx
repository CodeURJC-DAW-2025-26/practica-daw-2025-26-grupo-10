import { Link, useNavigate } from "react-router";
import {Button} from "react-bootstrap";

export default function Error403() {
    const navigate = useNavigate();

    return (
        <main className="d-flex align-items-center justify-content-center min-vh-100">
            <div className="text-center py-5">
                <h1 className="display-1 fw-bold">403</h1>
                <p className="lead mb-4">Acceso denegado</p>
                <p className="text-muted mb-4">
                    No tienes permisos para acceder a esta página.
                </p>
                <div className="d-flex justify-content-center gap-3">
                    <Link to="/" className="btn btn-primary">
                        Volver al inicio
                    </Link>
                    <button
                        className="btn btn-outline-secondary"
                        onClick={() => navigate(-1)}
                    >
                        Regresar
                    </button>
                </div>
            </div>
        </main>
    );
}
