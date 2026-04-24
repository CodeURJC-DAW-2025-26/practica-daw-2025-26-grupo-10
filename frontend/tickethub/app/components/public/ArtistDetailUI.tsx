import { Link } from "react-router";
import type { Artist } from "~/models/Artist";
import type { EventBasic } from "~/models/EventBasic";

interface Props {
    artist: Artist;
    eventsIncoming: EventBasic[];
    lastEvents: EventBasic[];
}

export default function ArtistDetailUI({ artist, eventsIncoming, lastEvents }: Props) {
    return (
        <div className="container my-5">
            <div className="card text-center">
                <img
                    src={`/api/v1/public/artists/${artist.artistID}/image`}
                    className="rounded-circle mx-auto mt-4"
                    width="150"
                    height="150"
                    style={{ objectFit: "cover" }}
                    alt={artist.artistName}
                />
                <div className="card-body">
                    <h3>{artist.artistName}</h3>
                    <p>{artist.info}</p>
                    <h5>Próximos eventos</h5>
                    {eventsIncoming.length === 0 ? (
                        <p>No hay eventos próximos para este artista.</p>
                    ) : (
                        <ul className="list-group mb-3">
                            {eventsIncoming.map((event) => (
                                <li key={event.eventID} className="list-group-item">{event.name}</li>
                            ))}
                        </ul>
                    )}


                    <h5>Últimos eventos realizados</h5>
                    {lastEvents.length === 0 ? (
                        <p>No hay eventos realizados de este artista.</p>
                    ) : (
                        <ul className="list-group mb-3">
                            {lastEvents.map((event) => (
                                <li key={event.eventID} className="list-group-item">{event.name}</li>
                            ))}
                        </ul>
                    )}

                    <div className="d-flex justify-content-center gap-2 mt-3">
                        {artist.instagram ? (
                            <a
                                href={`https://www.instagram.com/${artist.instagram}/`}
                                className="btn btn-outline-primary btn-sm"
                                target="_blank"
                                rel="noreferrer"
                            >
                                Instagram
                            </a>
                        ) : (
                            <span className="btn btn-outline-primary btn-sm disabled opacity-50">
                                Instagram
                            </span>
                        )}

                        {artist.twitter ? (
                            <a
                                href={`https://www.x.com/${artist.twitter}`}
                                className="btn btn-outline-info btn-sm"
                                target="_blank"
                                rel="noreferrer"
                            >
                                Twitter
                            </a>
                        ) : (
                            <span className="btn btn-outline-info btn-sm disabled opacity-50">
                                Twitter
                            </span>
                        )}
                    </div>

                    <div className="card-body" style={{ textAlign: "center" }}>
                        <Link to="/public/artists" className="btn btn-primary">Volver a artistas</Link>
                    </div>
                </div>
            </div>
        </div >
    )
}