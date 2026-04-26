import { useActionState, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { Container, Form, Button, Alert, Table, Row, Col } from "react-bootstrap";
import { getArtists, createEvent, updateEvent, getEvent, uploadEventImage } from "~/services/event-service";
import type { ArtistBasic } from "~/models/ArtistBasic";
import type { SessionBasic } from "~/models/SessionBasic";
import type { Event } from "~/models/Event";
import { API_URL } from "~/services/homeService";
import type ZoneBasic from "~/models/ZoneBasic";

const TARGET_AGE_OPTIONS = [
  { value: 0, label: "Niños pequeños" },
  { value: 1, label: "Niños" },
  { value: 2, label: "Adolescentes" },
  { value: 3, label: "Adultos jóvenes" },
  { value: 4, label: "Adultos" },
  { value: 5, label: "Adultos sénior" },
  { value: 6, label: "Ancianos" },
];

export default function CreateEvent() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = !!id;

  const [artists, setArtists] = useState<ArtistBasic[]>([]);
  const [event, setEvent] = useState<Event | null>(null);
  const [isPendingLoad, setIsPendingLoad] = useState(true);

  async function loadData() {
    setIsPendingLoad(true);
    try {
      const artistsData = await getArtists();
      setArtists(artistsData);
      if (isEditing) {
        const eventData = await getEvent(id);
        setEvent(eventData);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setIsPendingLoad(false);
    }
  }

  useEffect(() => { loadData(); }, [id]);

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
      discountIds: formData.get("discounts") as number[] | null,
      zones: formData.get("zones") as ZoneBasic[] | null,
      sessions: formData.get("sessions") as SessionBasic[] | null
    };

    const images = formData.getAll("images") as File[];

    try {
      if (isEditing) {
        await updateEvent(Number(id), updateData);
        uploadEventImages(Number(id), images);
        navigate("/admin/events");
      } else {
        const created = await createEvent(createData);
        uploadEventImages(Number(id), images);
        navigate(`/admin/events/edit/${created.eventID}`);
      }
      return { success: true, error: null };
    } catch (err) {
      console.error(err);
      return { success: false, error: isEditing ? "Error al guardar los cambios" : "Error al crear el evento" };
    }
  }

  const [state, formAction, isPending] = useActionState(submitAction, { success: false, error: null });

  if (isPendingLoad) return <p>Cargando...</p>;

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
          <Form.Select name="artistId" required disabled={isPending}>
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
            event.discounts && event.discounts.length > 0 ? (
              <ul className="mb-0">
                {event.discounts.map((discount) => (
                  <li key={discount.discountName}>
                    {discount.discountName} ({discount.amount}{discount.percentage ? "%" : "€"})
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-muted mb-0">Aún no hay descuentos asociados a este evento</p>
            )
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
                    <img
                      src={`${API_URL}/public/events/${event.eventID}/images/${index + 1}`}
                      className="img-fluid border"
                      alt="Imagen del evento"
                    />
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
