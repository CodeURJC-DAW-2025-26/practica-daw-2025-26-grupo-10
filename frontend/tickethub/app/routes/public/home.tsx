import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getIndexData, API_URL } from "~/services/homeService";
import type { ArtistBasic } from "~/models/ArtistBasic";
import type { EventBasic } from "~/models/EventBasic";
import type { IndexResponse } from "~/models/IndexResponse";


export default function Home() {
  const [data, setData] = useState<IndexResponse | null>(null);
  const [isPending, setIsPending] = useState(false);

  const navigate = useNavigate();

  async function loadData() {
    setIsPending(true);
    try {
      const result = await getIndexData();
      setData(result);
    } catch (error) {
      console.error(error);
    } finally {
      setIsPending(false);
    }
  }

  useEffect(() => { loadData(); }, []);

  if (isPending) {
    return <p>Cargando...</p>
  }

  if (!data) {
    return <p>Error al cargar la página principal</p>
  }

  return (
    <div className="d-flex flex-column min-vh-100">
      <main className="flex-fill">

        <section className="hero-section text-center">
          <div className="container text-center">
            <h1 className="display-5 fw-bold">
              Descubre y compra entradas para los mejores eventos
            </h1>
            <p className="lead">
              Conciertos, festivales, teatro y mucho más en un solo lugar.
            </p>
            <button className="btn btn-primary" onClick={() => navigate('/public/events')}>
              Explorar eventos
            </button>
          </div>
        </section>

        <div className="container my-5">

          <section className="mb-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2>Eventos más vendidos</h2>
              <a href="/public/events" className="btn btn-outline-primary btn-sm">
                Ver todos
              </a>
            </div>
            <div className="row g-4">
              {data.eventsTop.map((event: EventBasic) => (
                <div className="col-md-4" key={event.eventID}>
                  <div className="card h-100">
                    {event.mainImage && (
                      <img
                        src={`${API_URL}/public/events/${event.eventID}/images/1`}
                        className="card-img-top"
                        alt={event.mainImage.imageName}
                      />
                    )}
                    <div className="card-body">
                      <h5 className="card-title">{event.name}</h5>
                      <p className="mb-1">{event.category}</p>
                      <p className="text-muted">{event.place}</p>
                      <button className="btn btn-outline-primary" onClick={() => navigate(`/public/events/${event.eventID}`)}>
                        Ver evento
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <h2 className="mt-5 mb-4">Recomendaciones para ti</h2>
            <div className="row g-4">
              {data.recommendedEvents.length > 0 ? (
                data.recommendedEvents.map((event: EventBasic) => (
                  <div className="col-md-4" key={event.eventID}>
                    <div className="card h-100">
                      {event.mainImage && (
                        <img
                          src={`${API_URL}/public/events/${event.eventID}/images/1`}
                          className="card-img-top"
                          alt={event.mainImage.imageName}
                        />
                      )}
                      <div className="card-body">
                        <h5 className="card-title">{event.name}</h5>
                        <p className="text-muted">{event.place}</p>
                        <button className="btn btn-outline-primary" onClick={() => navigate(`/public/events/${event.eventID}`)}>
                          Ver evento
                        </button>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <p>No tenemos información suficiente para recomendarte un evento</p>
              )}
            </div>
          </section>

          <section className="mb-5 pb-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2>Artistas</h2>
              <a href="/public/artists" className="btn btn-outline-primary btn-sm">
                Ver todos
              </a>
            </div>
            <div className="row g-4">
              {data.artists.map((artist: ArtistBasic) => (
                <div className="col-md-3" key={artist.artistID}>
                  <div className="card text-center h-100">
                    {artist.artistImage && (
                      <img
                        src={`${API_URL}/public/artists/${artist.artistID}/image`}
                        className="card-img-top rounded-circle mx-auto"
                        alt={artist.artistName}
                      />
                    )}
                    <div className="card-body">
                      <h6 className="card-title">{artist.artistName}</h6>
                      <button className="btn btn-outline-primary" onClick={() => navigate(`/public/artists/${artist.artistID}`)}>
                        Ver artista
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </section>

          <section>
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2>Disponibles próximamente</h2>
              <a href="/public/events" className="btn btn-outline-primary btn-sm">
                Ver todos
              </a>
            </div>
            <div className="row g-4">
              {data.eventsBottom.map((event: EventBasic) => (
                <div className="col-md-6" key={event.eventID}>
                  <div className="card h-100">
                    <div className="card-body">
                      <h5 className="card-title">{event.name}</h5>
                      <p className="mb-1">Categoría: {event.category}</p>
                      <p className="text-muted">{event.place}</p>
                      <button className="btn btn-outline-primary" onClick={() => navigate(`/public/events/${event.eventID}`)}>
                        Más información
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </section>

        </div>
      </main>
    </div>
  );
}