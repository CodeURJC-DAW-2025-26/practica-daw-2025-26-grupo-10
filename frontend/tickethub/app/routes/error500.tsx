import { Link, useNavigate } from "react-router";
import { Button } from "react-bootstrap";

export default function Error500() {
  const navigate = useNavigate();

  return (
    <main className="d-flex align-items-center justify-content-center min-vh-100">
      <div className="text-center py-5">
        <h1 className="display-1 fw-bold">500</h1>
        <p className="lead mb-4">Error interno del servidor</p>
        <p className="text-muted mb-4">
          Algo ha salido mal en el servidor. Por favor, inténtalo de nuevo más tarde.
        </p>
        <div className="d-flex justify-content-center gap-3">
          <Link to="/" className="btn btn-primary">
            Volver al inicio
          </Link>
          <Button variant="outline-secondary" onClick={() => navigate(-1)}>
            Regresar
          </Button>
        </div>
      </div>
    </main>
  );
}
