import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getEvents, deleteEvent } from "~/services/events-service";
import type { EventBasic } from "~/models/EventBasic";

export default function ManageEvents() {

  const navigate = useNavigate();

  const [events, setEvents] = useState<EventBasic[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(0);
  const [deleteError, setDeleteError] = useState<string | null>(null);

  async function loadEvents(reset: boolean = false) {
    const currentPage = reset ? 0 : page;
    const size = 10;        //Default value to show the events 10 by 10
    
    if (reset) {
      setIsPending(true);
    } else {
      setIsLoadingMore(true);
    }

    try {
      const data = await getEvents(currentPage, size, null, null, null);
      setEvents(reset ? data : (prev) => [...prev, ...data]);
      setIsLast(data.length < 10);
      setPage(currentPage + 1);
    } catch (err) {
      console.error(err);
    } finally {
      setIsPending(false);
    }
  }

  async function handleDelete(eventID: number) {
    const confirmed = window.confirm("¿Estás seguro de que quieres eliminar este evento?");
    if (!confirmed) return;

    setDeleteError(null);
    try {
      await deleteEvent(eventID);
      setEvents((prev) => prev.filter((e) => e.eventID !== eventID));
    } catch (err) {
      console.error(err);
      setDeleteError("Error al eliminar el evento");
    }
  }

  useEffect(() => { loadEvents(true); }, []);

  return (
    <div className="container my-5">
      <h2>Gestión de Eventos</h2>

      {deleteError && <p className="text-danger">{deleteError}</p>}

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

          {!isLast && (
            <div className="row mt-4">
            <div className="col-12 text-end">
                <button
                className="btn btn-outline-secondary"
                onClick={() => loadEvents(false)}
                disabled={isLoadingMore}
                >
                {isLoadingMore ? "Cargando..." : "Cargar más"}
                </button>
            </div>
            </div>
          )}
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