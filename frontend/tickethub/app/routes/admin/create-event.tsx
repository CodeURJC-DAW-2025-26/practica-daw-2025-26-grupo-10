import { useActionState, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { getArtists, createEvent, updateEvent, getEvent } from "~/services/event-service";
import type { ArtistBasic } from "~/models/ArtistBasic";
import type { SessionBasic } from "~/models/SessionBasic";
import type { ZoneBasic } from "~/models/ZoneBasic";
import type { Event } from "~/models/Event";
import type { ImageBasic } from "~/models/ImageBasic";
import { API_URL } from "~/services/homeService";

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

  async function submitAction(
    prevState: { success: boolean; error: string | null },
    formData: FormData
  ) {
    const data = {
      name: formData.get("name") as string,
      category: formData.get("category") as string,
      place: formData.get("place") as string,
      artistId: Number(formData.get("artistId")),
      targetAge: Number(formData.get("targetAge")),
      capacity: Number(formData.get("capacity")),
      eventImages: formData.get("images") as ImageBasic[] | null,
      discountIds: formData.get("discounts") as number[] | null,
      zones: formData.get("zones") as ZoneBasic[] | null,
      sessions: formData.get("sessions") as SessionBasic[] | null
    };

    try {
      if (isEditing) {
        await updateEvent(Number(id), data);
      } else {
        const created = await createEvent(data);
        navigate(`/admin/events/edit/${created.eventID}`);
        return { success: true, error: null };
      }
      navigate("/admin/events");
      return { success: true, error: null };
    } catch (err) {
      console.error(err);
      return { success: false, error: isEditing ? "Error al guardar los cambios" : "Error al crear el evento" };
    }
  }

  const [state, formAction, isPending] = useActionState(submitAction, {
    success: false,
    error: null,
  });

  if (isPendingLoad) return <p>Cargando...</p>;

  return (
    <main className="container my-5 flex-grow-1">
        <h2 className="text-center">{isEditing ? "Editar Evento" : "Crear Evento"}</h2>

        {state.error && (
          <div className="alert alert-danger alert-dismissible fade show" role="alert">
            {state.error}
          </div>
        )}

        <form action={formAction}>

            {/* Name */}
            <p className="fw-bold mb-1">Nombre del evento</p>
            <input
            name="name"
            className="form-control mb-3"
            defaultValue={event?.name ?? ""}
            required
            disabled={isPending}
            />

            {/* Capacity (edition) */}
            {isEditing && event && (
            <p className="fw-bold mb-1">Capacidad total: {event.capacity}</p>
            )}
            
            {!isEditing && (
            <>
                <p className="fw-bold mb-1">Capacidad</p>
                <input
                name="capacity"
                type="number"
                className="form-control mb-3"
                required
                disabled={isPending}
                />
            </>
            )}

            {isEditing && (
            <input type="hidden" name="capacity" value={event?.capacity ?? 0} />
            )}

            {/* Category */}
            <p className="fw-bold mb-1">Categoría</p>
            <input
            name="category"
            type="text"
            className="form-control mb-3"
            defaultValue={event?.category ?? ""}
            disabled={isPending}
            />

            {/* Place */}
            <p className="fw-bold mb-1">Lugar</p>
            <input
            name="place"
            type="text"
            className="form-control mb-3"
            defaultValue={event?.place ?? ""}
            disabled={isPending}
            />

            {/* Artist */}
            <p className="fw-bold mb-1">Artista principal</p>
            <select name="artistId" className="form-select mb-3" required disabled={isPending}>
            <option value="" disabled>Selecciona un artista</option>
            {artists.map((artist) => (
                <option
                key={artist.artistID}
                value={artist.artistID}
                >
                {artist.artistName}
                </option>
            ))}
            </select>

            {/* Target age */}
            <p className="fw-bold mb-1">Edad objetivo</p>
            <select name="targetAge" className="form-select mb-3" disabled={isPending}>
            {TARGET_AGE_OPTIONS.map((opt) => (
                <option
                key={opt.value}
                value={opt.value}
                selected={event?.targetAge === opt.value}
                >
                {opt.label}
                </option>
            ))}
            </select>

            {/* Sessions (edition) */}
            <p className="fw-bold mb-1">Sesiones</p>
            {isEditing ? (
            <button
                type="button"
                className="btn btn-outline-secondary mb-3"
                onClick={() => navigate(`/admin/events/${id}/sessions`)}
                disabled={isPending}
            >
                Gestionar Sesiones
            </button>
            ) : (
            <p className="text-muted mb-3">Primero guarda el evento para gestionar sesiones</p>
            )}

            {/* Zonez (edition) */}
            <p className="fw-bold mb-1">Zonas del evento</p>
            {isEditing && event ? (
            <>
                {event.zones && event.zones.length > 0 ? (
                <table className="table mb-2">
                    <thead>
                    <tr>
                        <th>Zona</th>
                        <th>Capacidad</th>
                        <th>Precio</th>
                    </tr>
                    </thead>
                    <tbody>
                    {event.zones.map((zone) => (
                        <tr key={zone.id}>
                        <td>{zone.name}</td>
                        <td>{zone.capacity}</td>
                        <td>{zone.price} €</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                ) : (
                <p className="text-muted">Aún no hay ninguna zona asociada a este evento</p>
                )}
                <button
                type="button"
                className="btn btn-outline-secondary mb-3"
                onClick={() => navigate(`/admin/events/${id}/zones`)}
                disabled={isPending}
                >
                Gestionar zonas
                </button>
            </>
            ) : (
            <p className="text-muted mb-3">Primero guarda el evento para gestionar sus zonas</p>
            )}

            {/* Discounts (edition) */}
            <p className="fw-bold mb-1">Descuentos asociados</p>
            {isEditing && event ? (
            event.discounts && event.discounts.length > 0 ? (
                <ul className="mb-3">
                {event.discounts.map((discount) => (
                    <li key={discount.discountName}>
                    {discount.discountName} ({discount.amount}{discount.percentage ? "%" : "€"})
                    </li>
                ))}
                </ul>
            ) : (
                <p className="text-muted mb-3">Aún no hay descuentos asociados a este evento</p>
            )
            ) : (
            <p className="text-muted mb-3">Primero guarda el evento para poder gestionar descuentos</p>
            )}

            {/* Images (edition) */}
            {isEditing && event && (
            <>
                <p className="fw-bold mb-1">Imágenes actuales</p>
                <div className="row g-3 mb-4">
                {event.eventImages && event.eventImages.length > 0 ? (
                    event.eventImages.map((image, index) => (
                    <div className="col-6 col-md-3" key={image.imageID}>
                        <img
                        src={`${API_URL}/public/events/${event.eventID}/images/${index + 1}`}
                        className="img-fluid border"
                        alt="Imagen del evento"
                        />
                    </div>
                    ))
                ) : (
                    <p className="text-muted">No hay imágenes para este evento.</p>
                )}
                </div>
            </>
            )}

            {/* SUBIR IMÁGENES */}
            <p className="fw-bold mb-1">Subir nuevas imágenes</p>
            <input type="file" name="images" className="form-control mb-3" multiple disabled={isPending} />

            {/* BOTONES */}
            <div className="d-flex justify-content-between mt-3">
            <button
                type="button"
                className="btn btn-outline-primary"
                onClick={() => navigate("/admin/events")}
                disabled={isPending}
            >
                Cancelar
            </button>
            <button type="submit" className="btn btn-success" disabled={isPending}>
                {isPending ? "Guardando..." : isEditing ? "Guardar cambios" : "Crear evento"}
            </button>
            </div>

        </form>
    </main>
  );
}