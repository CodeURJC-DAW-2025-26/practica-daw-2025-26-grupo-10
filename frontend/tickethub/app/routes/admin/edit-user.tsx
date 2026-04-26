import { useEffect, useState, type SetStateAction } from "react";
import { useParams, useNavigate } from "react-router";
import { Container, Card, Form, Button, Alert, Row, Col } from "react-bootstrap";
import { getUserById, updateUser } from "~/services/adminService";
import type { UserDTO } from "~/models/User";

export default function EditUser() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [formData, setFormData] = useState<UserDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!id) return;
        getUserById(id)
            .then((data: SetStateAction<UserDTO | null>) => setFormData(data))
            .catch((err: { message: SetStateAction<string | null> }) => setError(err.message))
            .finally(() => setLoading(false));
    }, [id]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData || !id) return;
        setSaving(true);
        try {
            await updateUser(formData.userID, formData);
            navigate("/admin/users");
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al guardar");
            setSaving(false);
        }
    };

    if (loading) return <Container className="py-5 text-center">Cargando...</Container>;
    if (!formData) return <Container className="py-5 text-center text-danger">Usuario no encontrado</Container>;

    return (
        <Container className="py-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Editar Usuario #{id}</h2>
                <Button variant="outline-secondary" onClick={() => navigate("/admin/users")}>Volver</Button>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            <Row>
                <Col md={8} className="offset-md-2">
                    <Card className="shadow-sm">
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Nombre de Usuario</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={formData.username || ""}
                                        onChange={e => setFormData({ ...formData, username: e.target.value })}
                                        required
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Correo Electrónico</Form.Label>
                                    <Form.Control
                                        type="email"
                                        value={formData.email || ""}
                                        onChange={e => setFormData({ ...formData, email: e.target.value })}
                                        required
                                    />
                                </Form.Group>
                                <Form.Group className="mb-4">
                                    <Form.Check
                                        type="switch"
                                        id="adminSwitch"
                                        label="¿Es Administrador?"
                                        checked={formData.admin}
                                        onChange={e => setFormData({ ...formData, admin: e.target.checked })}
                                    />
                                </Form.Group>
                                <div className="d-grid">
                                    <Button type="submit" variant="primary" disabled={saving}>
                                        {saving ? "Guardando..." : "Guardar Cambios"}
                                    </Button>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}
