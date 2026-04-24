import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router";
import { getZones, deleteZone } from "~/services/zones-service";
import type Zone from "~/models/Zone";
import { ConfirmDialog } from "~/components/confirmDialog/ConfirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";

export default function ManageZones() {
  const { eventId } = useParams<{ eventId: string }>();
  const [zones, setZones] = useState<Zone[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const navigate = useNavigate();

  const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
  const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

  async function loadZones() {
    if (!eventId) return;
    setIsPending(true);
    try {
      const data = await getZones(eventId);
      setZones(data);
    } catch {
      setLoadError("No se pudieron cargar las zonas.");
    } finally {
      setIsPending(false);
    }
  }

  useEffect(() => {
    loadZones();
  }, [eventId]);

  function handleDelete(zoneId: number) {
    confirm("¿Estás seguro de que deseas eliminar esta zona?", async () => {
      try {
        await deleteZone(eventId!, zoneId);
        setDeleteSuccess("Zona eliminada correctamente.");
        await loadZones();
      } catch (err) {
        console.error(err);
        setDeleteError("No se pudo eliminar la zona.");
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

      <h2>Gestión de Zonas</h2>

      {loadError && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {loadError}
          <button type="button" className="btn-close" onClick={() => setLoadError(null)} aria-label="Cerrar" />
        </div>
      )}
      {deleteError && <p className="alert alert-danger">{deleteError}</p>}
      {deleteSuccess && <p className="alert alert-success">{deleteSuccess}</p>}

      {isPending ? (
        <p>Cargando zonas...</p>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Capacidad</th>
              <th>Precio</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {zones.length === 0 && (
              <tr>
                <td colSpan={4} className="text-center text-muted">
                  No hay zonas registradas para este evento.
                </td>
              </tr>
            )}
            {zones.map((z) => (
              <tr key={z.id}>
                <td>{z.name}</td>
                <td>{z.capacity}</td>
                <td>{z.price} €</td>
                <td className="d-flex gap-2">
                  <Link
                    to={`/admin/events/${eventId}/zones/${z.id}/edit`}
                    className="btn btn-sm btn-primary"
                  >
                    Editar
                  </Link>
                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleDelete(z.id)}
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
          onClick={() => navigate(`/admin/events/${eventId}/edit`)}
        >
          Volver al evento
        </button>
        <Link
          to={`/admin/events/${eventId}/zones/new`}
          className="btn btn-success"
        >
          + Agregar zona
        </Link>
      </div>
    </div>
  );
}
