import { Link } from "react-router";
import { Container, Card, Button, ListGroup } from "react-bootstrap";
import type { Artist } from "~/models/Artist";

interface Props {
    artist: Artist;
}

export default function ArtistDetailUI({ artist }: Props) {
    return (
            <Card className="text-center col-12 col-sm-10 col-md-9 col-lg-5 mx-auto mb-5 mt-5">
                <img
                    src={`/api/v1/public/artists/${artist.artistID}/image`}
                    className="rounded-circle mx-auto mt-4"
                    width="150"
                    height="150"
                    style={{ objectFit: "cover" }}
                    alt={artist.artistName}
                />
                <Card.Body>
                    <h3>{artist.artistName}</h3>
                    <p>{artist.info}</p>

                    <h5>Próximos eventos</h5>
                    {artist.eventsIncoming.length === 0 ? (
                        <p>No hay eventos próximos para este artista.</p>
                    ) : (
                        <ListGroup className="mb-3" variant="flush">
                            {artist.eventsIncoming.map((event) => (
                                <ListGroup.Item key={event.eventID}>{event.name}</ListGroup.Item>
                            ))}
                        </ListGroup>
                    )}

                    <h5>Últimos eventos realizados</h5>
                    {artist.lastEvents.length === 0 ? (
                        <p>No hay eventos realizados de este artista.</p>
                    ) : (
                        <ListGroup className="mb-3" variant="flush">
                            {artist.lastEvents.map((event) => (
                                <ListGroup.Item key={event.eventID}>{event.name}</ListGroup.Item>
                            ))}
                        </ListGroup>
                    )}

                    <div className="d-flex justify-content-center gap-2 mt-3">
                        {artist.instagram ? (
                            <a href={`https://www.instagram.com/${artist.instagram}/`} className="btn btn-outline-primary btn-sm" target="_blank" rel="noreferrer">
                                Instagram
                            </a>
                        ) : (
                            <span className="btn btn-outline-primary btn-sm disabled opacity-50">Instagram</span>
                        )}

                        {artist.twitter ? (
                            <a href={`https://www.x.com/${artist.twitter}`} className="btn btn-outline-primary btn-sm" target="_blank" rel="noreferrer">
                                Twitter
                            </a>
                        ) : (
                            <span className="btn btn-outline-primary btn-sm disabled opacity-50">Twitter</span>
                        )}
                    </div>

                    <div className="mt-3">
                        <Link to="/public/artists" className="btn btn-primary">Volver a artistas</Link>
                    </div>
                </Card.Body>
            </Card>
    );
}
