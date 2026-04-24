import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { getEvent } from "~/services/event-service";
import type { Event } from "~/models/Event";
import { API_URL } from "~/services/homeService";

export default function Event() {

  const { id } = useParams();
  const navigate = useNavigate();

  const [event, setEvent] = useState<Event | null>(null);
  const [isPending, setIsPending] = useState(true);
  const [activeIndex, setActiveIndex] = useState(0);

  async function loadEvent() {
    if (!id) return;
    setIsPending(true);
    try {
      const data = await getEvent(id);
      setEvent(data);
    } catch (err) {
      console.error(err);
    } finally {
      setIsPending(false);
    }
  }

  useEffect(() => { loadEvent(); }, [id]);

  if (isPending) return <p>Cargando...</p>;

  if (!event) return <p>Evento no encontrado</p>;

  const images = event.eventImages ?? [];

  return (
    <div className="container my-5">
      <div className="card">
        <div className="row">

          {/* Carousel */}
          <div className="col-md-6" style={{ alignItems: "center", padding: "2%" }}>
            {images.length > 0 && (
              <div id="carouselEvent" className="carousel slide">
                <div className="carousel-inner">
                  {images.map((image, index) => (
                    <div
                      className={`carousel-item ${index === activeIndex ? "active" : ""}`}
                      key={image.imageID}
                    >
                      <img
                        src={`${API_URL}/public/events/${event.eventID}/images/${index + 1}`}
                        className="d-block w-100"
                        alt="Imagen del evento"
                      />
                    </div>
                  ))}
                </div>

                <button
                  className="carousel-control-prev"
                  type="button"
                  onClick={() => setActiveIndex((prev) => (prev - 1 + images.length) % images.length)}
                >
                  <i className="bi bi-caret-left"></i>
                  <span className="visually-hidden">Previous</span>
                </button>

                <button
                  className="carousel-control-next"
                  type="button"
                  onClick={() => setActiveIndex((prev) => (prev + 1) % images.length)}
                >
                  <i className="bi bi-caret-right"></i>
                  <span className="visually-hidden">Next</span>
                </button>
              </div>
            )}
          </div>

          {/* Info */}
          <div className="col-md-6">
            <div className="card-body">
              <h2>{event.name}</h2>

              <strong><p>Sesiones disponibles:</p></strong>
              <div className="d-flex align-items-center gap-2 mb-3">
                <ul>
                  {event.sessions.map((session) => (
                    <li key={session.sessionID}>
                      <p>{new Date(session.date).toLocaleString("es-ES")}</p>
                    </li>
                  ))}
                </ul>
              </div>

              <p><strong>Lugar:</strong> {event.place}</p>
              <p><strong>Categoría:</strong> {event.category}</p>
              <p><strong>Artista:</strong> {event.artist.artistName}</p>
              <p><strong>Capacidad:</strong> {event.capacity}</p>

              <button
                className="btn btn-outline-secondary"
                onClick={() => navigate(`/public/artists/${event.artist.artistID}`)}
              >
                Ver artista
              </button>
            </div>
          </div>
        </div>

        <hr />

        {/* Buttons */}
        <div className="row mb-3">
          <div className="col d-flex justify-content-between">
            <button
              className="btn btn-success"
              onClick={() => navigate("/public/events")}
            >
              Volver a eventos
            </button>
            <button
              className="btn btn-success"
              onClick={() => navigate(`/public/purchase/${event.eventID}`)}
            >
              Comprar entradas
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}