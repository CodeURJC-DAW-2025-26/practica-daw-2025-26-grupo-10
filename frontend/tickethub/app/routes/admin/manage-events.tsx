import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getEventsAdmin, deleteEvent } from "~/services/events-service";
import type { EventBasic } from "~/models/EventBasic";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";
import { ConfirmDialog } from "~/components/confirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";
import {Button} from "react-bootstrap";

export default function ManageEvents() {

  const navigate = useNavigate();

  const [events, setEvents] = useState<EventBasic[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);

  const { error: deleteError, setError: setDeleteError,
          success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();

  const {isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm} = useConfirmDialog();

  async function loadEvents() {

    try {
      const data = await getEventsAdmin();
      setEvents(data);
    } catch {
      setLoadError("No se pudieron cargar los eventos")
    } finally {
      setIsPending(false);
    }
  }

  async function handleDelete(eventID: number) {
    confirm("¿Estás seguro de que quieres eliminar este evento?", async () => {
      try {
        await deleteEvent(eventID);
        setEvents((prev) => prev.filter((e) => e.eventID !== eventID));
        setDeleteSuccess("Evento eliminado correctamente")
      } catch (err) {
        console.error(err);
        setDeleteError("Error al eliminar el evento");
      }
    });
  }

  useEffect(() => { loadEvents(); }, []);

  return (
    <div className="container my-5">
      {isNotConfirmed && (
        <ConfirmDialog
          message={message}
          onConfirm={handleConfirm}
          onCancel={handleCancel}
        />
      )}
      <h2>Gestión de Eventos</h2>

      {loadError && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {loadError}
          <Button className="btn-close" onClick={() => setLoadError(null)} aria-label="Cerrar" />
        </div>
      )}
      
      {deleteError && <p className="alert alert-danger">{deleteError}</p>}
      {deleteSuccess && <p className="alert alert-success">{deleteSuccess}</p>}

      {isPending ? (
        <p>Cargando eventos...</p>
      ) : (
        <>
          <table className="table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {events.map((event) => (
                <tr key={event.eventID}>
                  <td>{event.name}</td>
                  <td>{event.category}</td>
                  <td>
                    <button
                      className="btn btn-sm btn-primary me-2"
                      onClick={() => navigate(`/admin/events/edit/${event.eventID}`)}
                    >
                      Editar
                    </button>
                    <button
                      className="btn btn-sm btn-danger"
                      onClick={() => handleDelete(event.eventID)}
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}

      <div className="row mb-3">
        <div className="col d-flex justify-content-start">
          <button
            className="btn btn-outline-primary btn-success"
            onClick={() => navigate("/admin/admin")}
          >
            Volver
          </button>
        </div>
      </div>
    </div>
  );
}