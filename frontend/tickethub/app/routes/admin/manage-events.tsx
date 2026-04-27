import { useState } from "react";
import { useLoaderData, useNavigate } from "react-router";
import { Container, Table, Button, Alert, Row } from "react-bootstrap";
import { getEventsAdmin, deleteEvent } from "~/services/events-service";
import type { EventBasic } from "~/models/EventBasic";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";
import { ConfirmDialog } from "~/components/confirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";

export async function clientLoader() {
  const events = await getEventsAdmin();
  return { events };
}

export default function ManageEvents() {

  const {events: initialEvents} = useLoaderData<typeof clientLoader>();
  const navigate = useNavigate();

  const [events, setEvents] = useState<EventBasic[]>(initialEvents);

  const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
  const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

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


  return (
    <div className="d-flex align-items-center justify-content-center py-5">
      <Container className="my-5">
        {isNotConfirmed && (
          <ConfirmDialog
            message={message}
            onConfirm={handleConfirm}
            onCancel={handleCancel}
          />
        )}
        <h2>Gestión de Eventos</h2>
        
        {deleteError && <p className="alert alert-danger">{deleteError}</p>}
        {deleteSuccess && <p className="alert alert-success">{deleteSuccess}</p>}

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