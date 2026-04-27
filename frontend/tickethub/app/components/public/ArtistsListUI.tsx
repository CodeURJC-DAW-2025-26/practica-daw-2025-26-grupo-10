import { Link } from "react-router";
import { Container, Row, Col, Card, Button, Form } from "react-bootstrap";
import type { ArtistBasic } from "~/models/ArtistBasic";

interface Props {
    artists: ArtistBasic[];
    searchQuery: string;
    onSearchChange: (query: string) => void;
    onLoadMore: () => void;
    hasMore: boolean;
}

export default function ArtistsListUI({ artists, searchQuery, onSearchChange, onLoadMore, hasMore }: Props) {
    return (
        <main className="flex-fill">
            <Container className="my-5">
                <h2 className="mb-4">Filtrar artistas</h2>

                <Row className="mb-4">
                    <Col md={12}>
                        <Form.Control
                            type="text"
                            placeholder="Buscar artista..."
                            value={searchQuery}
                            onChange={(e) => onSearchChange(e.target.value)}
                        />
                    </Col>
                </Row>

                <Row className="g-4">
                    {artists.length === 0 ? (
                        <p className="text-muted">No se encontraron artistas.</p>
                    ) : (
                        artists.map((artist) => (
                            <Col key={artist.artistID} md={3}>
                                <Card className="text-center">
                                    <Card.Img
                                        variant="top"
                                        src={`/api/v1/public/artists/${artist.artistID}/image`}
                                        className="rounded-circle mx-auto mt-3"
                                        style={{ width: "150px", height: "150px", objectFit: "cover" }}
                                        alt={artist.artistName}
                                    />
                                    <Card.Body>
                                        <Card.Title as="h5">{artist.artistName}</Card.Title>
                                        <Link to={`/public/artists/${artist.artistID}`} className="btn btn-primary">
                                            Ver artista
                                        </Link>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))
                    )}
                </Row>

                {hasMore && (
                    <div className="text-end mt-4">
                        <Button variant="outline-secondary" onClick={onLoadMore}>
                            Cargar más
                        </Button>
                    </div>
                )}
            </Container>
        </main>
    );
}
