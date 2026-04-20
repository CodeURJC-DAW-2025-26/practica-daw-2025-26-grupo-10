import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router";
import { getZones, deleteZone } from "~/services/zones-service";
import type { Zone } from "~/services/zones-service";

export default function ManageZones() {
  const { eventId } = useParams<{ eventId: string }>();
  const [zones, setZones] = useState<Zone[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  async function loadZones() {
    if (!eventId) return;
    setIsPending(true);
    try {
      const data = await getZones(eventId);
      setZones(data);
    } catch {
      setError("No se pudieron cargar las zonas.");
    } finally {
      setIsPending(false);
    }
  }

  useEffect(() => {
    loadZones();
  }, [eventId]);

  async function handleDelete(zoneId: number) {
    if (!confirm("¿Eliminar esta zona?")) return;
    try {
      await deleteZone(eventId!, zoneId);
      await loadZones();
    } catch {
      setError("No se pudo eliminar la zona.");
    }
  }

  return (
    <div className="container my-5">
      <h2>Gestión de Zonas</h2>

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
