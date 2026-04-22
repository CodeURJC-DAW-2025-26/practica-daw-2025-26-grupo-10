import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import { getDiscounts, deleteDiscount } from "~/services/discounts-service";
import type { Discount } from "~/models/Discount";

export default function ManageDiscounts() {
  const [discounts, setDiscounts] = useState<Discount[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  async function loadDiscounts() {
    setIsPending(true);
    try {
      const data = await getDiscounts();
      setDiscounts(data);
    } catch (err) {
      setError("No se pudieron cargar los descuentos.");
    } finally {
      setIsPending(false);
    }
  }

  useEffect(() => {
    loadDiscounts();
  }, []);

  async function handleDelete(id: number) {
    if (!confirm("¿Eliminar este descuento?")) return;
    try {
      await deleteDiscount(id);
      await loadDiscounts();
    } catch {
      setError("No se pudo eliminar el descuento.");
    }
  }

  return (
    <div className="container my-5">
      <h2>Gestión de Descuentos</h2>

      {error && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {error}
          <button
            type="button"
            className="btn-close"
            onClick={() => setError(null)}
            aria-label="Cerrar"
          />
        </div>
      )}

      {isPending ? (
        <p>Cargando descuentos...</p>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Valor</th>
              <th>Tipo</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {discounts.length === 0 && (
              <tr>
                <td colSpan={4} className="text-center text-muted">
                  No hay descuentos registrados.
                </td>
              </tr>
            )}
            {discounts.map((d) => (
              <tr key={d.discountID}>
                <td>{d.discountName}</td>
                <td>
                  {d.amount} {d.percentage ? "%" : "€"}
                </td>
                <td>{d.percentage ? "Porcentaje" : "Cantidad fija"}</td>
                <td className="d-flex gap-2">
                  <Link
                    to={`/admin/discounts/${d.discountID}/edit`}
                    className="btn btn-sm btn-primary"
                  >
                    Editar
                  </Link>
                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleDelete(d.discountID)}
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <div className="d-flex justify-content-between mt-3">
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/admin/admin")}
        >
          Volver
        </button>
        <Link to="/admin/discounts/new" className="btn btn-success">
          + Crear descuento
        </Link>
      </div>
    </div>
  );
}
