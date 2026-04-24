import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import { getDiscounts, deleteDiscount } from "~/services/discounts-service";
import type Discount from "~/models/Discount";
import { ConfirmDialog } from "~/components/confirmDialog/ConfirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";

export default function ManageDiscounts() {
  const [discounts, setDiscounts] = useState<Discount[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const navigate = useNavigate();

  const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
  const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

  async function loadDiscounts() {
    setIsPending(true);
    try {
      const data = await getDiscounts();
      setDiscounts(data);
    } catch {
      setLoadError("No se pudieron cargar los descuentos.");
    } finally {
      setIsPending(false);
    }
  }

  useEffect(() => {
    loadDiscounts();
  }, []);

  function handleDelete(id: number) {
    confirm("¿Estás seguro de que deseas eliminar este descuento?", async () => {
      try {
        await deleteDiscount(id);
        setDeleteSuccess("Descuento eliminado correctamente.");
        await loadDiscounts();
      } catch (err) {
        console.error(err);
        setDeleteError("No se pudo eliminar el descuento.");
      }
    });
  }

  return (
    <div className="container my-5">
      {isNotConfirmed && (
        <ConfirmDialog
          message={message}
          onConfirm={handleConfirm}
          onCancel={handleCancel}
        />
      )}

      <h2>Gestión de Descuentos</h2>

      {loadError && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {loadError}
          <button type="button" className="btn-close" onClick={() => setLoadError(null)} aria-label="Cerrar" />
        </div>
      )}
      {deleteError && <p className="alert alert-danger">{deleteError}</p>}
      {deleteSuccess && <p className="alert alert-success">{deleteSuccess}</p>}

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
