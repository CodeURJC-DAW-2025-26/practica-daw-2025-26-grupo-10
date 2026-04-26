import { Link } from "react-router";
import { changePassword } from "~/services/user-service";
import { useActionState } from "react";
import { Container, Card, Form, Button, Alert, Row, Col } from "react-bootstrap";
import type { ChangePasswordBasic } from "~/models/UserBasic";

interface ActionState {
    success?: string;
    error?: string;
}

export default function ChangePassword() {

    async function handleUpdatePassword(prevState: ActionState | null, formData: FormData) {
        const oldPassword = formData.get("oldPassword") as string;
        const newPassword = formData.get("newPassword") as string;
        const confirmationPassword = formData.get("confirmationPassword") as string;

        const changePasswordBasic: ChangePasswordBasic = { oldPassword, newPassword, confirmationPassword };

        try {
            const response = await changePassword(changePasswordBasic);
            return { success: response };
        } catch (error: any) {
            return { error: error.message };
        }
    }

    const [state, updatePassword, isPending] = useActionState(handleUpdatePassword, null);

    return (
        <Container className="my-5">
            <Row className="justify-content-center">
                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <h2 className="mb-4">Cambiar contraseña</h2>
                            <Form action={updatePassword}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Contraseña actual</Form.Label>
                                    <Form.Control type="password" name="oldPassword" required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Nueva Contraseña</Form.Label>
                                    <Form.Control type="password" name="newPassword" required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Repetir nueva contraseña</Form.Label>
                                    <Form.Control type="password" name="confirmationPassword" required />
                                </Form.Group>

                                {state?.success && <Alert variant="success">{state.success}</Alert>}
                                {state?.error && <Alert variant="danger">{state.error}</Alert>}

                                <div className="d-flex gap-2">
                                    <Button type="submit" variant="primary" disabled={isPending}>
                                        {isPending ? "Actualizando..." : "Actualizar"}
                                    </Button>
                                    <Link to="/clients/profile" className="btn btn-outline-secondary">
                                        Volver al perfil
                                    </Link>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}
