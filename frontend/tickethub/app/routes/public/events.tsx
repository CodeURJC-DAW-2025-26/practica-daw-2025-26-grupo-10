import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { Container, Row, Col, Card, Button, Form } from "react-bootstrap";
import { getEventsPublic, getCategories } from "~/services/events-service";
import type { EventBasic } from "~/models/EventBasic";
import { API_URL } from "~/services/homeService";
import { useStore } from "~/store/useStore";

export default function Events() {
  const navigate = useNavigate();

  const [events, setEvents] = useState<EventBasic[]>([]);
  const [categories, setCategories] = useState<string[]>([]);
  const [isPending, setIsPending] = useState(true);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [isLast, setIsLast] = useState(false);
  const [page, setPage] = useState(0);

  const { eventsSearch, setEventsSearch } = useStore();
  const [filterCategory, setFilterCategory] = useState("");
  const [filterDate, setFilterDate] = useState("");

  async function loadCategories() {
    try {
      const data = await getCategories();
      setCategories(data);
    } catch (err) {
      console.error(err);
    }
  }

  async function loadEvents(reset: boolean = false) {
    const currentPage = reset ? 0 : page;
    const size = 5;
    if (reset) setIsPending(true);
    else setIsLoadingMore(true);
    try {
      const data = await getEventsPublic(currentPage, size, eventsSearch, filterCategory, filterDate);
      setEvents(reset ? data : (prev) => [...prev, ...data]);
      setIsLast(data.length < 5);
      setPage(currentPage + 1);
    } catch (err) {
      console.error(err);
    } finally {
      setIsPending(false);
      setIsLoadingMore(false);
    }
  }

  useEffect(() => { loadCategories(); }, []);
  useEffect(() => { loadEvents(true); }, [filterCategory, filterDate]);
  useEffect(() => {
    const timer = setTimeout(() => { loadEvents(true); }, 500);
    return () => clearTimeout(timer);
  }, [eventsSearch]);

  return (
    <Container className="my-5">
      <h2 className="mb-4">Próximos eventos</h2>

      <Row className="mb-4">
        <Col md={4}>
          <Form.Control
            type="date"
            value={filterDate}
            onChange={(e) => setFilterDate(e.target.value)}
          />
        </Col>
        <Col md={4}>
          <Form.Select
            value={filterCategory}
            onChange={(e) => setFilterCategory(e.target.value)}
          >
            <option value="">Todos</option>
            {categories.map((cat) => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </Form.Select>
        </Col>
        <Col md={4}>
          <Form.Control
            type="text"
            placeholder="Artista"
            value={eventsSearch}
            onChange={(e) => setEventsSearch(e.target.value)}
          />
        </Col>
      </Row>

      {isPending ? (
        <p>Cargando...</p>
      ) : (
        <Row className="g-4">
          {events.length === 0 ? (
            <p>No se han encontrado eventos</p>
          ) : (
            events.map((event: EventBasic) => (
              <Col md={4} key={event.eventID}>
                <Card className="h-100">
                  {event.mainImage && (
                    <Card.Img
                      variant="top"
                      src={`${API_URL}/public/events/${event.eventID}/images/1`}
                      alt={event.name}
                    />
                  )}
                  <Card.Body>
                    <Card.Title as="h5">{event.name}</Card.Title>
                    <p className="mb-1">{event.category}</p>
                    <p className="text-muted">{event.place}</p>
                    <Button
                      variant="primary"
                      size="sm"
                      onClick={() => navigate(`/public/events/${event.eventID}`)}
                    >
                      Ver evento
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            ))
          )}
        </Row>
      )}

      {!isLast && (
        <Row className="mt-4">
          <Col xs={12} className="text-end">
            <Button
              variant="outline-secondary"
              onClick={() => loadEvents(false)}
              disabled={isLoadingMore}
            >
              {isLoadingMore ? "Cargando..." : "Cargar más"}
            </Button>
          </Col>
        </Row>
      )}
    </Container>
  );
}
