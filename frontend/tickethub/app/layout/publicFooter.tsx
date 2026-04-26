import { Link } from "react-router";
import { Container, Row, Col } from "react-bootstrap";

export const PublicFooter = () => {
    return (
        <footer className="mt-auto">
            <Container className="text-black py-4">
                <Row>
                    <Col md={4} className="mb-3">
                        <h5>Sobre nosotros</h5>
                        <p>
                            TicketHub conecta a los usuarios con eventos, artistas y recintos,
                            ofreciendo una experiencia de compra de entradas simple y segura.
                        </p>
                    </Col>

                    <Col md={4} className="mb-3">
                        <h5>Enlaces rápidos</h5>
                        <ul className="list-unstyled">
                            <li>
                                <Link to="/" className="text-decoration-none">
                                    Inicio
                                </Link>
                            </li>
                            <li>
                                <Link to="/public/events" className="text-decoration-none">
                                    Eventos
                                </Link>
                            </li>
                            <li>
                                <Link to="/public/artists" className="text-decoration-none">
                                    Artistas
                                </Link>
                            </li>
                        </ul>
                    </Col>

                    <Col md={4} className="mb-3">
                        <h5>Síguenos</h5>
                        <ul className="list-inline">
                            <li className="list-inline-item">
                                <a href="#" className="text-white">
                                    <i className="bi bi-facebook"></i>
                                </a>
                            </li>
                            <li className="list-inline-item">
                                <a href="#" className="text-white">
                                    <i className="bi bi-twitter"></i>
                                </a>
                            </li>
                            <li className="list-inline-item">
                                <a href="#" className="text-white">
                                    <i className="bi bi-instagram"></i>
                                </a>
                            </li>
                        </ul>
                    </Col>
                </Row>

                <hr className="border-secondary" />

                <div className="text-center">
                    <p className="mb-0">&copy; 2026 TicketHub. Todos los derechos reservados.</p>
                </div>
            </Container>
        </footer>
    );
};
