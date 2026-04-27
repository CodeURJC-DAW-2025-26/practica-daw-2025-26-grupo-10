import { useState } from "react";
import { useLoaderData, useNavigate, useParams } from "react-router";
import { Row, Col, Card, Button } from "react-bootstrap";
import { getEvent } from "~/services/event-service";
import type { Event } from "~/models/Event";
import { API_URL } from "~/services/homeService";

export async function clientLoader({ params }: { params: { id: string } }) {
  const data = await getEvent(params.id);
  return { event: data };
}

export default function Event() {
  const navigate = useNavigate();

  const { event: initialEvent } = useLoaderData<typeof clientLoader>();
  const [event] = useState<Event>(initialEvent);
  const [activeIndex, setActiveIndex] = useState(0);

  const images = event.eventImages ?? [];

  return (
    <Card className="col-12 col-sm-12 col-md-9 col-lg-8 mx-auto mb-5 mt-5">
      <Row>
        <Col md={6} style={{ alignItems: "center", padding: "2%" }}>
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
        </Col>

        <Col md={6}>
          <Card.Body>
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

            <Button
              variant="outline-secondary"
              onClick={() => navigate(`/public/artists/${event.artist.artistID}`)}
            >
              Ver artista
            </Button>
          </Card.Body>
        </Col>
      </Row>

      <hr />

      <Row className="mb-3">
        <Col className="d-flex justify-content-between">
          <Button variant="success" onClick={() => navigate("/public/events")}>
            Volver a eventos
          </Button>
          <Button variant="success" onClick={() => navigate(`/public/purchase/${event.eventID}`)}>
            Comprar entradas
          </Button>
        </Col>
      </Row>
    </Card>
  );
}
