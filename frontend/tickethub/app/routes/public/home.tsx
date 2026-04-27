import { Link, useLoaderData, useNavigate } from "react-router";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { getIndexData, API_URL } from "~/services/homeService";
import type { ArtistBasic } from "~/models/ArtistBasic";
import type { EventBasic } from "~/models/EventBasic";

export async function clientLoader() {
  const data = await getIndexData();
  return { data };
}

export default function Home() {
  const {data} = useLoaderData<typeof clientLoader>();

  const navigate = useNavigate();

  return (
    <>
      <section className="hero-section text-center">
        <Container className="text-center">
          <h1 className="display-5 fw-bold">
            Descubre y compra entradas para los mejores eventos
          </h1>
          <p className="lead">
            Conciertos, festivales, teatro y mucho más en un solo lugar.
          </p>
          <Button variant="primary" onClick={() => navigate('/public/events')}>
            Explorar eventos
          </Button>
        </Container>
      </section>

      <Container className="my-5">
        <section className="mb-5">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h2>Eventos más vendidos</h2>
            <a href="/public/events" className="btn btn-outline-primary btn-sm">Ver todos</a>
          </div>
          <Row className="g-4">
            {data.eventsTop.map((event: EventBasic) => (
              <Col md={4} key={event.eventID}>
                <Card className="h-100">
                  {event.mainImage && (
                    <Card.Img
                      variant="top"
                      src={`${API_URL}/public/events/${event.eventID}/images/1`}
                      alt={event.mainImage.imageName}
                    />
                  )}
                  <Card.Body>
                    <Card.Title as="h5">{event.name}</Card.Title>
                    <p className="mb-1">{event.category}</p>
                    <p className="text-muted">{event.place}</p>
                    <Button variant="outline-primary" onClick={() => navigate(`/public/events/${event.eventID}`)}>
                      Ver evento
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          <h2 className="mt-5 mb-4">Recomendaciones para ti</h2>
          <Row className="g-4">
            {data.recommendedEvents.length > 0 ? (
              data.recommendedEvents.map((event: EventBasic) => (
                <Col md={4} key={event.eventID}>
                  <Card className="h-100">
                    {event.mainImage && (
                      <Card.Img
                        variant="top"
                        src={`${API_URL}/public/events/${event.eventID}/images/1`}
                        alt={event.mainImage.imageName}
                      />
                    )}
                    <Card.Body>
                      <Card.Title as="h5">{event.name}</Card.Title>
                      <p className="text-muted">{event.place}</p>
                      <Button variant="outline-primary" onClick={() => navigate(`/public/events/${event.eventID}`)}>
                        Ver evento
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              ))
            ) : (
              <p>No tenemos información suficiente para recomendarte un evento</p>
            )}
          </Row>
        </section>

        <section className="mb-5 pb-4">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h2>Artistas</h2>
            <Link to="/public/events" className="btn btn-outline-primary btn-sm">Ver todos</Link>
          </div>
          <Row className="g-4">
            {data.artists.map((artist: ArtistBasic) => (
              <Col md={3} key={artist.artistID}>
                <Card className="text-center h-100">
                  {artist.artistImage && (
                    <Card.Img
                      variant="top"
                      src={`${API_URL}/public/artists/${artist.artistID}/image`}
                      className="rounded-circle mx-auto"
                      alt={artist.artistName}
                    />
                  )}
                  <Card.Body>
                    <Card.Title as="h6">{artist.artistName}</Card.Title>
                    <Button variant="outline-primary" onClick={() => navigate(`/public/artists/${artist.artistID}`)}>
                      Ver artista
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </section>

        <section>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h2>Disponibles próximamente</h2>
            <a href="/public/events" className="btn btn-outline-primary btn-sm">Ver todos</a>
          </div>
          <Row className="g-4">
            {data.eventsBottom.map((event: EventBasic) => (
              <Col md={6} key={event.eventID}>
                <Card className="h-100">
                  <Card.Body>
                    <Card.Title as="h5">{event.name}</Card.Title>
                    <p className="mb-1">Categoría: {event.category}</p>
                    <p className="text-muted">{event.place}</p>
                    <Button variant="outline-primary" onClick={() => navigate(`/public/events/${event.eventID}`)}>
                      Más información
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </section>
      </Container>
    </>
  );
}
