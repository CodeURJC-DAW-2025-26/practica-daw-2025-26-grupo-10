import { Link } from "react-router";

export const PublicFooter = () => {
    return (
        <footer className="mt-auto">
            <div className="container text-white py-4">

                <div className="row">

                    <div className="col-md-4 mb-3">
                        <h5>Sobre nosotros</h5>
                        <p>
                            TicketHub conecta a los usuarios con eventos, artistas y recintos,
                            ofreciendo una experiencia de compra de entradas simple y segura.
                        </p>
                    </div>

                    <div className="col-md-4 mb-3">
                        <h5>Enlaces rápidos</h5>
                        <ul className="list-unstyled">
                            <li>
                                <Link to="/" className="text-decoration-none text-white">
                                    Inicio
                                </Link>
                            </li>
                            <li>
                                <Link to="/public/events" className="text-decoration-none text-white">
                                    Eventos
                                </Link>
                            </li>
                            <li>
                                <Link to="/public/artists" className="text-decoration-none text-white">
                                    Artistas
                                </Link>
                            </li>
                        </ul>
                    </div>

                    <div className="col-md-4 mb-3">
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
                    </div>

                </div>

                <hr className="border-secondary" />

                <div className="text-center">
                    <p className="mb-0">
                        &copy; 2026 TicketHub. Todos los derechos reservados.
                    </p>
                </div>
            </div>
        </footer>
    );
};