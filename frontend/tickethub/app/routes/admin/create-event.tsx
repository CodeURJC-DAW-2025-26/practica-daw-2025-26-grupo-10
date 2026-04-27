import { useActionState, useState } from "react";
import { useLoaderData, useNavigate, useParams } from "react-router";
import { Container, Form, Button, Alert, Table, Row, Col } from "react-bootstrap";
import { getArtists, createEvent, updateEvent, getEvent, uploadEventImage, deleteEventImage } from "~/services/event-service";
import type { Event } from "~/models/Event";
import { API_URL } from "~/services/homeService";
import { getDiscounts } from "~/services/discounts-service";
import type Discount from "~/models/Discount";

const TARGET_AGE_OPTIONS = [
  { value: 0, label: "Niños pequeños" },
  { value: 1, label: "Niños" },
  { value: 2, label: "Adolescentes" },
  { value: 3, label: "Adultos jóvenes" },
  { value: 4, label: "Adultos" },
  { value: 5, label: "Adultos sénior" },
  { value: 6, label: "Ancianos" },
];

export async function clientLoader({ params }: { params: { id?: string } }) {
  const [artists, event, allDiscounts] = await Promise.all([
    getArtists(),
    params.id ? getEvent(params.id) : Promise.resolve(null),
    getDiscounts()
  ]);
  return { artists, event, allDiscounts };
}

export default function CreateEvent() {

  const {artists, event: initialEvent, allDiscounts} = useLoaderData<typeof clientLoader>();

  const [event, setEvent] = useState<Event | null>(initialEvent);
  const [discounts, setDiscounts] = useState<Discount[]>(initialEvent?.discounts ?? []);
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = !!id;

  function handleAddDiscount(discountID: number) {
    const discount = allDiscounts.find((d) => d.discountID === discountID);
    if (discount && !discounts.find((d) => d.discountID === discountID)) {
      setDiscounts((prev) => [...prev, discount]);
    }
  }

  function handleRemoveDiscount(discountID: number) {
    setDiscounts((prev) => prev.filter((d) => d.discountID !== discountID));
  }

  async function handleDeleteImage(index: number) {
    if (!event) return;
    try {
      await deleteEventImage(event.eventID, index);
      setEvent((prev) => prev ? {
        ...prev,
        eventImages: prev.eventImages.filter((_, i) => i !== index)
      } : prev);
    } catch (err) {
      console.error(err);
    }
  }

  async function uploadEventImages(eventID: number, images: File[]) {
    const validImages = images.filter((img) => img.size > 0);
    for (const image of validImages) await uploadEventImage(eventID, image);
  }

  async function submitAction(
    prevState: { success: boolean; error: string | null },
    formData: FormData
  ) {
    const createData = {
      name: formData.get("name") as string,
      category: formData.get("category") as string,
      place: formData.get("place") as string,
      artistId: Number(formData.get("artistId")),
      targetAge: Number(formData.get("targetAge")),
      capacity: Number(formData.get("capacity"))
    };

    const updateData = {
      ...createData,
      discountIds: discounts.map((d) => d.discountID),
      zones: event?.zones ?? [],
      sessions: event?.sessions ?? []
    };

    const images = formData.getAll("images") as File[];

    try {
      if (isEditing) {
        await updateEvent(Number(id), updateData);
        await uploadEventImages(Number(id), images);
        navigate("/admin/events");
      } else {
        const created = await createEvent(createData);
        await uploadEventImages(created.eventID, images);
        navigate(`/admin/events/edit/${created.eventID}`);
      }
      return { success: true, error: null };
    } catch (err) {
      console.error(err);
      return { success: false, error: isEditing ? "Error al guardar los cambios" : "Error al crear el evento" };
    }
  }

  const [state, formAction, isPending] = useActionState(submitAction, { success: false, error: null });

  return (
    <Container as="main" className="my-5 flex-grow-1">
      <h2 className="text-center">{isEditing ? "Editar Evento" : "Crear Evento"}</h2>

      {state.error && <Alert variant="danger">{state.error}</Alert>}

      <Form action={formAction}>
        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Nombre del evento</Form.Label>
          <Form.Control name="name" defaultValue={event?.name ?? ""} required disabled={isPending} />
        </Form.Group>

        {isEditing && event && (
          <p className="fw-bold mb-3">Capacidad total: {event.capacity}</p>
        )}

        {!isEditing && (
          <Form.Group className="mb-3">
            <Form.Label className="fw-bold">Capacidad</Form.Label>
            <Form.Control name="capacity" type="number" required disabled={isPending} />
          </Form.Group>
        )}

        {isEditing && <input type="hidden" name="capacity" value={event?.capacity ?? 0} />}

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Categoría</Form.Label>
          <Form.Control name="category" type="text" defaultValue={event?.category ?? ""} disabled={isPending} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Lugar</Form.Label>
          <Form.Control name="place" type="text" defaultValue={event?.place ?? ""} disabled={isPending} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Artista principal</Form.Label>
          <Form.Select name="artistId" defaultValue={event?.artist?.artistID ?? ""} required disabled={isPending}>
            <option value="" disabled>Selecciona un artista</option>
            {artists.map((artist) => (
              <option key={artist.artistID} value={artist.artistID}>{artist.artistName}</option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Edad objetivo</Form.Label>
          <Form.Select name="targetAge" defaultValue={event?.targetAge ?? 0} disabled={isPending}>
            {TARGET_AGE_OPTIONS.map((opt) => (
              <option key={opt.value} value={opt.value}>{opt.label}</option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Sesiones</Form.Label>
          {isEditing ? (
            <div>
              <Button type="button" variant="outline-secondary" onClick={() => navigate(`/admin/events/${id}/sessions`)} disabled={isPending}>
                Gestionar Sesiones
              </Button>
            </div>
          ) : (
            <p className="text-muted">Primero guarda el evento para gestionar sesiones</p>
          )}
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Zonas del evento</Form.Label>
          {isEditing && event ? (
            <>
              {event.zones && event.zones.length > 0 ? (
                <Table className="mb-2">
                  <thead>
                    <tr><th>Zona</th><th>Capacidad</th><th>Precio</th></tr>
                  </thead>
                  <tbody>
                    {event.zones.map((zone) => (
                      <tr key={zone.id}>
                        <td>{zone.name}</td><td>{zone.capacity}</td><td>{zone.price} €</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                <p className="text-muted">Aún no hay ninguna zona asociada a este evento</p>
              )}
              <Button type="button" variant="outline-secondary" onClick={() => navigate(`/admin/events/${id}/zones`)} disabled={isPending}>
                Gestionar zonas
              </Button>
            </>
          ) : (
            <p className="text-muted">Primero guarda el evento para gestionar sus zonas</p>
          )}
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Descuentos asociados</Form.Label>
          {isEditing && event ? (
            <>
              {discounts.length > 0 ? (
                <ul className="mb-2">
                  {discounts.map((discount) => (
                    <li key={discount.discountID} className="d-flex align-items-center gap-2 mb-1">
                      {discount.discountName} ({discount.amount}{discount.percentage ? "%" : "€"})
                      <Button size="sm" variant="outline-danger" type="button"
                        onClick={() => handleRemoveDiscount(discount.discountID)}>
                        Eliminar
                      </Button>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-muted mb-2">Aún no hay descuentos asociados</p>
              )}
              <Form.Select className="w-auto"
                onChange={(e) => { if (e.target.value) handleAddDiscount(Number(e.target.value)); e.target.value = ""; }}
              >
                <option value="">Añadir descuento...</option>
                {allDiscounts
                  .filter((d) => !discounts.find((ed) => ed.discountID === d.discountID))
                  .map((d) => (
                    <option key={d.discountID} value={d.discountID}>
                      {d.discountName} ({d.amount}{d.percentage ? "%" : "€"})
                    </option>
                  ))}
              </Form.Select>
            </>
          ) : (
            <p className="text-muted mb-0">Primero guarda el evento para poder gestionar descuentos</p>
          )}
        </Form.Group>

        {isEditing && event && (
          <Form.Group className="mb-4">
            <Form.Label className="fw-bold">Imágenes actuales</Form.Label>
            <Row className="g-3">
              {event.eventImages && event.eventImages.length > 0 ? (
                event.eventImages.map((image, index) => (
                  <Col xs={6} md={3} key={image.imageID}>
                    <div className="image-overlay-container border">
                      <button
                        type="button"
                        className="btn-delete-overlay"
                        onClick={() => handleDeleteImage(index)}
                        title="Eliminar imagen"
                      >
                        <i className="bi bi-trash-fill"></i>
                        <span className="fw-bold small">ELIMINAR</span>
                      </button>
                      <img
                        src={`${API_URL}/public/events/${event.eventID}/images/${index + 1}`}
                        className="img-event-admin"
                        alt="Imagen del evento"
                      />
                    </div>
                  </Col>
                ))
              ) : (
                <p className="text-muted">No hay imágenes para este evento.</p>
              )}
            </Row>
          </Form.Group>
        )}

        <Form.Group className="mb-3">
          <Form.Label className="fw-bold">Subir nuevas imágenes</Form.Label>
          <Form.Control type="file" name="images" multiple disabled={isPending} />
        </Form.Group>

        <div className="d-flex justify-content-between mt-3">
          <Button type="button" variant="outline-primary" onClick={() => navigate("/admin/events")} disabled={isPending}>
            Cancelar
          </Button>
          <Button type="submit" variant="success" disabled={isPending}>
            {isPending ? "Guardando..." : isEditing ? "Guardar cambios" : "Crear evento"}
          </Button>
        </div>
      </Form>
    </Container>
  );
}
