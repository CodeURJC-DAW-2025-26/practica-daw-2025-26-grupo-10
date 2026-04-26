import { Link } from "react-router";
import { Container, Card, Button, Row, Col } from "react-bootstrap";
import { useStore } from "~/store/useStore";
import { API_URL } from "~/services/homeService";
import { useNavigate } from "react-router";

export default function Profile() {
    const user = useStore((state) => state.user);
    const logout = useStore((state) => state.logout);
    const navigate = useNavigate();

    async function handleLogout() {
        try {
            await logout();
            navigate("/");
        } catch (error) {
            console.error("Error al cerrar sesión", error);
        }
    }

    return (
        <Container className="my-5">
            <Row className="justify-content-center">
                <Col md={6}>
                    <Card className="text-center">
                        <Card.Body>
                            <img
                                src={`${API_URL}/users/${user!.userID}/image`}
                                alt="Avatar"
                                className="rounded-circle mb-3"
                                style={{ width: "100px", height: "100px", objectFit: "cover" }}
                            />
                            <h4>{user!.username}</h4>
                            <p className="text-muted">{user!.email}</p>
                            <div className="d-grid gap-2">
                                <Link to="/clients/profile/edit" className="btn btn-outline-primary">
                                    Editar Perfil
                                </Link>
                                <Link to="/purchases/me" className="btn btn-outline-primary">
                                    Mis Compras
                                </Link>
                                <Link to="/clients/me/password" className="btn btn-outline-primary">
                                    Cambiar Contraseña
                                </Link>
                                <Button variant="danger" onClick={handleLogout}>
                                    Cerrar Sesión
                                </Button>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}
