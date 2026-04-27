import { useState } from "react";
import { Link, useLoaderData, useNavigate, useParams } from "react-router";
import { Container, Table, Button, Alert } from "react-bootstrap";
import { getZones, deleteZone } from "~/services/zones-service";
import type Zone from "~/models/Zone";
import { ConfirmDialog } from "~/components/confirmDialog/ConfirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";

export async function clientLoader({ params }: { params: { eventId: string } }) {
  const zones = await getZones(params.eventId);
  return { zones };
}

export default function ManageZones() {
  const { zones: initialZones } = useLoaderData<typeof clientLoader>();
  const { eventId } = useParams<{ eventId: string }>();
  const [zones, setZones] = useState<Zone[]>(initialZones);
  
  const navigate = useNavigate();

  const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
  const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

  function handleDelete(zoneId: number) {
    confirm("¿Estás seguro de que deseas eliminar esta zona?", async () => {
      try {
        await deleteZone(eventId!, zoneId);
        setDeleteSuccess("Zona eliminada correctamente.");
        setZones((prev) => prev.filter((z) => z.id !== zoneId));
      } catch (err) {
        console.error(err);
        setDeleteError("No se pudo eliminar la zona.");
      }
    });
  }

  return (
    <Container className="my-5">
      {isNotConfirmed && (
        <ConfirmDialog message={message} onConfirm={handleConfirm} onCancel={handleCancel} />
      )}

      <h2>Gestión de Zonas</h2>

      {deleteError && <Alert variant="danger">{deleteError}</Alert>}
      {deleteSuccess && <Alert variant="success">{deleteSuccess}</Alert>}

      <Table className="table">
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
                <Link to={`/admin/events/${eventId}/zones/${z.id}/edit`} className="btn btn-sm btn-primary">Editar</Link>
                <Button size="sm" variant="danger" onClick={() => handleDelete(z.id)}>Eliminar</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <div className="d-flex justify-content-between mt-3">
        <Button variant="outline-secondary" onClick={() => navigate(`/admin/events/${eventId}`)}>
          Volver al evento
        </Button>
        <Link to={`/admin/events/${eventId}/zones/new`} className="btn btn-success">+ Agregar zona</Link>
      </div>
    </Container>
  );
}
