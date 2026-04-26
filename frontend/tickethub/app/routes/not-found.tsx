import { Link, useNavigate } from "react-router";
import { Button } from "react-bootstrap";

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <main className="d-flex align-items-center justify-content-center min-vh-100">
      <div className="text-center py-5">
        <h1 className="display-1 fw-bold">404</h1>
        <p className="lead mb-4">Página no encontrada</p>
        <p className="text-muted mb-4">
          La ruta que estás buscando no existe o ha sido movida.
        </p>
        <div className="d-flex justify-content-center gap-3">
          <Link to="/" className="btn btn-primary">Volver al inicio</Link>
          <Button variant="outline-secondary" onClick={() => navigate(-1)}>Regresar</Button>
        </div>
      </div>
    </main>
  );
}
