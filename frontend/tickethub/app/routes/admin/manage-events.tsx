import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { Container, Table, Button, Alert, Row } from "react-bootstrap";
import { getEventsAdmin, deleteEvent } from "~/services/events-service";
import type { EventBasic } from "~/models/EventBasic";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";
import { ConfirmDialog } from "~/components/confirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";

export default function ManageEvents() {
  const navigate = useNavigate();
  const [events, setEvents] = useState<EventBasic[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);

  const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
  const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

  async function loadEvents() {
    try {
      const data = await getEventsAdmin();
      setEvents(data);
    } catch {
      setLoadError("No se pudieron cargar los eventos");
    } finally {
      setIsPending(false);
    }
  }

  async function handleDelete(eventID: number) {
    confirm("¿Estás seguro de que quieres eliminar este evento?", async () => {
      try {
        await deleteEvent(eventID);
        setEvents((prev) => prev.filter((e) => e.eventID !== eventID));
        setDeleteSuccess("Evento eliminado correctamente");
      } catch (err) {
        console.error(err);
        setDeleteError("Error al eliminar el evento");
      }
    });
  }

  useEffect(() => { loadEvents(); }, []);

  return (
    <div className="d-flex align-items-center justify-content-center py-5">
      <Container className="my-5">
        {isNotConfirmed && (
          <ConfirmDialog message={message} onConfirm={handleConfirm} onCancel={handleCancel} />
        )}
        <h2>Gestión de Eventos</h2>

        {loadError && (
          <Alert variant="danger" dismissible onClose={() => setLoadError(null)}>
            {loadError}
          </Alert>
        )}
        {deleteError && <Alert variant="danger">{deleteError}</Alert>}
        {deleteSuccess && <Alert variant="success">{deleteSuccess}</Alert>}

        {isPending ? (
          <p>Cargando eventos...</p>
        ) : (
          <Table>
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
                    <Button size="sm" variant="primary" className="me-2" onClick={() => navigate(`/admin/events/${event.eventID}`)}>
                      Editar
                    </Button>
                    <Button size="sm" variant="danger" onClick={() => handleDelete(event.eventID)}>
                      Eliminar
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}

        <Row>
          <div className="col d-flex justify-content-start">
            <Button variant="outline-primary" onClick={() => navigate("/admin")}>
              Volver
            </Button>
          </div>
        </Row>
      </Container>
    </div>
  );
}