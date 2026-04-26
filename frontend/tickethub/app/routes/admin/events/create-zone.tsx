import { useActionState, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { Container, Form, Button, Alert } from "react-bootstrap";
import { getZone, createZone, updateZone } from "~/services/zones-service";
import type Zone from "~/models/Zone";

export default function CreateZone() {
  const { eventId, id } = useParams<{ eventId: string; id?: string }>();
  const isEditing = !!id;
  const navigate = useNavigate();

  const [zone, setZone] = useState<Zone | null>(null);
  const [isLoadingZone, setIsLoadingZone] = useState(isEditing);

  useEffect(() => {
    if (!id || !eventId) return;
    setIsLoadingZone(true);
    getZone(eventId, id)
      .then(setZone)
      .catch(() => navigate(`/admin/events/${eventId}/zones`))
      .finally(() => setIsLoadingZone(false));
  }, [id, eventId]);

  async function formAction(_prev: { error: string | null }, formData: FormData) {
    const data = {
      name: formData.get("name") as string,
      capacity: parseInt(formData.get("capacity") as string, 10),
      price: parseFloat(formData.get("price") as string),
    };

    if (!data.name.trim()) return { error: "El nombre de la zona es obligatorio." };
    if (isNaN(data.capacity) || data.capacity <= 0) return { error: "La capacidad debe ser un número entero positivo." };
    if (isNaN(data.price) || data.price <= 0) return { error: "El precio debe ser un número positivo." };

    try {
      if (isEditing && id && eventId) await updateZone(eventId, id, data);
      else if (eventId) await createZone(eventId, data);
      navigate(`/admin/events/${eventId}/zones`);
      return { error: null };
    } catch {
      return { error: isEditing ? "No se pudo actualizar la zona." : "No se pudo crear la zona." };
    }
  }

  const [state, dispatchAction, isPending] = useActionState(formAction, { error: null });

  if (isLoadingZone) {
    return <Container className="my-5 text-center"><p>Cargando datos de la zona...</p></Container>;
  }

  return (
    <Container className="my-5">
      <h2 className="text-center mb-4">{isEditing ? "Editar Zona" : "Crear Zona"}</h2>

      {state.error && <Alert variant="danger">{state.error}</Alert>}

      <Form action={dispatchAction} className="col-md-6 mx-auto">
        <Form.Group className="mb-3">
          <Form.Label htmlFor="name">Nombre</Form.Label>
          <Form.Control id="name" type="text" name="name" defaultValue={zone?.name ?? ""} required disabled={isPending} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="capacity">Capacidad</Form.Label>
          <Form.Control id="capacity" type="number" min="1" step="1" name="capacity" defaultValue={zone?.capacity ?? ""} required disabled={isPending} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label htmlFor="price">Precio (€)</Form.Label>
          <Form.Control id="price" type="number" step="0.01" min="0.01" name="price" defaultValue={zone?.price ?? ""} required disabled={isPending} />
        </Form.Group>

        <div className="d-flex justify-content-between mt-4">
          <Button type="button" variant="outline-secondary" onClick={() => navigate(`/admin/events/${eventId}/zones`)} disabled={isPending}>
            Cancelar
          </Button>
          <Button type="submit" variant="success" disabled={isPending}>
            {isPending ? (isEditing ? "Actualizando..." : "Creando...") : (isEditing ? "Actualizar" : "Crear")}
          </Button>
        </div>
      </Form>
    </Container>
  );
}
