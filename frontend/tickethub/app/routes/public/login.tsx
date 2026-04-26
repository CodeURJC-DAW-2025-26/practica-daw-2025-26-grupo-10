import { useStore } from "~/store/useStore";
import { useNavigate } from "react-router";
import { useActionState } from "react";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

export default function Login() {
    const login = useStore((state) => state.login);
    const error = useStore((state) => state.error);
    const navigate = useNavigate();

    async function handleLogin(prevState: any, formData: FormData) {
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;
        try {
            await login(email, password);
            navigate("/clients/profile");
            return null;
        } catch {
            return null;
        }
    }

    const [_, formAction, isPending] = useActionState(handleLogin, null);

    return (
        <Container className="my-5">
            <Card className="col-md-5 mx-auto">
                <Card.Body>
                    <h3 className="mb-4 text-center">Iniciar sesión</h3>
                    <Form action={formAction}>
                        <Form.Group className="mb-3">
                            <Form.Label>Correo electrónico</Form.Label>
                            <Form.Control
                                name="email"
                                type="text"
                                placeholder="Correo electrónico"
                                disabled={isPending}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Contraseña</Form.Label>
                            <Form.Control
                                name="password"
                                type="password"
                                placeholder="Contraseña"
                                disabled={isPending}
                                required
                            />
                        </Form.Group>
                        {error && <Alert variant="danger">{error}</Alert>}
                        <Button type="submit" variant="primary" className="w-100" disabled={isPending}>
                            {isPending ? "Enviando..." : "Iniciar sesión"}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
}
