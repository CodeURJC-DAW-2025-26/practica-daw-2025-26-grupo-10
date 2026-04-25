import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getEvents, getCategories } from "~/services/events-service";
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

  const {eventsSearch, setEventsSearch} = useStore();
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
    const size = 5;   //Default value to show the events 5 by 5

    if (reset) {
      setIsPending(true);
    } else {
      setIsLoadingMore(true);
    }

    try {
      const data = await getEvents(currentPage, size, eventsSearch, filterCategory, filterDate);
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

  // Initial categories load
  useEffect(() => { loadCategories(); }, []);

  // Reload with every change at the filters
  useEffect(() => { loadEvents(true); }, [filterCategory, filterDate]);

  useEffect(() => {
    const timer = setTimeout(() => {loadEvents(true)}, 500);
    return () => clearTimeout(timer);
  }, [eventsSearch]);

  return (
    <div className="container my-5">
      <h2 className="mb-4">Próximos eventos</h2>

      {/* FILTERS */}
      <div className="row mb-4">
        <div className="col-md-4">
          <input
            type="date"
            className="form-control"
            value={filterDate}
            onChange={(e) => setFilterDate(e.target.value)}
          />
        </div>
        <div className="col-md-4">
          <select
            className="form-select"
            value={filterCategory}
            onChange={(e) => setFilterCategory(e.target.value)}
          >
            <option value="">Todos</option>
            {categories.map((cat) => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>
        <div className="col-md-4">
          <input
            type="text"
            className="form-control"
            placeholder="Artista"
            value={eventsSearch}
            onChange={(e) => setEventsSearch(e.target.value)}
          />
        </div>
      </div>

      {/* LIST */}
      {isPending ? (
        <p>Cargando...</p>
      ) : (
        <div className="row g-4">
          {events.length === 0 ? (
            <p>No se han encontrado eventos</p>
          ) : (
            events.map((event: EventBasic) => (
              <div className="col-md-4" key={event.eventID}>
                <div className="card h-100">
                  {event.mainImage && (
                    <img
                      src={`${API_URL}/public/events/${event.eventID}/images/1`}
                      className="card-img-top"
                      alt={event.name}
                    />
                  )}
                  <div className="card-body">
                    <h5 className="card-title">{event.name}</h5>
                    <p className="mb-1">{event.category}</p>
                    <p className="text-muted">{event.place}</p>
                    <button
                      className="btn btn-primary btn-sm"
                      onClick={() => navigate(`/public/events/${event.eventID}`)}
                    >
                      Ver evento
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      )}

      {/* Load more button */}
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
    </div>
  );
}