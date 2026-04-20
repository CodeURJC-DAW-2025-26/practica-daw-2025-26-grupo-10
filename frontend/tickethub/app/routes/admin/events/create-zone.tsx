import { useActionState, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { getZone, createZone, updateZone } from "~/services/zones-service";
import type { Zone } from "~/services/zones-service";

export default function CreateZone() {
  const { eventId, id } = useParams<{ eventId: string; id?: string }>();
  const isEditing = !!id;
  const navigate = useNavigate();

  const [zone, setZone] = useState<Zone | null>(null);
  const [isLoadingZone, setIsLoadingZone] = useState(isEditing);

  // Load existing zone data when editing
  useEffect(() => {
    if (!id || !eventId) return;
    setIsLoadingZone(true);
    getZone(eventId, id)
      .then(setZone)
      .catch(() => navigate(`/admin/events/${eventId}/zones`))
      .finally(() => setIsLoadingZone(false));
  }, [id, eventId]);

  async function formAction(
    _prev: { error: string | null },
    formData: FormData
  ) {
    const name = formData.get("name") as string;
    const capacity = parseInt(formData.get("capacity") as string, 10);
    const price = parseFloat(formData.get("price") as string);

    if (!name.trim()) {
      return { error: "El nombre de la zona es obligatorio." };
    }
    if (isNaN(capacity) || capacity <= 0) {
      return { error: "La capacidad debe ser un número entero positivo." };
    }
    if (isNaN(price) || price <= 0) {
      return { error: "El precio debe ser un número positivo." };
    }

    try {
      if (isEditing && id && eventId) {
        await updateZone(eventId, id, { name, capacity, price });
      } else if (eventId) {
        await createZone(eventId, { name, capacity, price });
      }
      navigate(`/admin/events/${eventId}/zones`);
      return { error: null };
    } catch (err) {
      return {
        error: isEditing
          ? "No se pudo actualizar la zona."
          : "No se pudo crear la zona.",
      };
    }
  }

  const [state, dispatchAction, isPending] = useActionState(formAction, {
    error: null,
  });

  if (isLoadingZone) {
    return (
      <div className="container my-5 text-center">
        <p>Cargando datos de la zona...</p>
      </div>
    );
  }

  return (
    <div className="container my-5">
      <h2 className="text-center mb-4">
        {isEditing ? "Editar Zona" : "Crear Zona"}
      </h2>

      {state.error && (
        <div className="alert alert-danger alert-dismissible fade show" role="alert">
          {state.error}
        </div>
      )}

      <form action={dispatchAction} className="col-md-6 mx-auto">
        <div className="mb-3">
          <label htmlFor="name" className="form-label">
            Nombre
          </label>
          <input
            id="name"
            type="text"
            className="form-control"
            name="name"
            defaultValue={zone?.name ?? ""}
            required
            disabled={isPending}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="capacity" className="form-label">
            Capacidad
          </label>
          <input
            id="capacity"
            type="number"
            min="1"
            step="1"
            className="form-control"
            name="capacity"
            defaultValue={zone?.capacity ?? ""}
            required
            disabled={isPending}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="price" className="form-label">
            Precio (€)
          </label>
          <input
            id="price"
            type="number"
            step="0.01"
            min="0.01"
            className="form-control"
            name="price"
            defaultValue={zone?.price ?? ""}
            required
            disabled={isPending}
          />
        </div>

        <div className="d-flex justify-content-between mt-4">
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => navigate(`/admin/events/${eventId}/zones`)}
            disabled={isPending}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="btn btn-success"
            disabled={isPending}
          >
            {isPending
              ? isEditing
                ? "Actualizando..."
                : "Creando..."
              : isEditing
              ? "Actualizar"
              : "Crear"}
          </button>
        </div>
      </form>
    </div>
  );
}
