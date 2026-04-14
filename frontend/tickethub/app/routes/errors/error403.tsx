import { Link, useNavigate } from "react-router";

export default function Error403() {
    const navigate = useNavigate();

    return (
        //header 

        <main className="flex-fill d-flex align-items-center justify-content-center">
            <div className="text-center">
                <h1 className="display-1">403</h1>
                <p className="lead">Acceso no autorizado</p>
                
                <p className="lead mb-4">No tienes permisos suficientes para acceder a este recurso.</p>

                <Link to="/" className="btn btn-primary me-2">Volver al inicio</Link>
                <button onClick={() => navigate(-1)} className="btn btn-outline-secondary">
                    Regresar
                </button>
            </div>
        </main>

        //footer 
    );
}