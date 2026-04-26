import { useActionState } from "react";
import { useNavigate } from "react-router";
import { useStore } from "~/store/useStore";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

export default function SignUp() {
    const signup = useStore((state) => state.signup);
    const error = useStore((state) => state.error);
    const navigate = useNavigate();

    async function handleSignup(prevState: any, formData: FormData) {
        const name = formData.get("name") as string;
        const surname = formData.get("surname") as string;
        const username = formData.get("username") as string;
        const email = formData.get("email") as string;
        const password = formData.get("password") as string;
        const passwordConfirmation = formData.get("passwordConfirmation") as string;
        try {
            await signup(name, surname, username, email, password, passwordConfirmation);
            navigate("/public/login");
            return null;
        } catch {
            return null;
        }
    }

    const [_, formAction, isPending] = useActionState(handleSignup, null);

    return (
        <Container className="my-5">
            <Card className="col-md-6 mx-auto">
                <Card.Body>
                    <h3 className="mb-4 text-center">Crear Cuenta</h3>
                    <Form action={formAction}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre</Form.Label>
                            <Form.Control type="text" name="name" placeholder="Nombre" required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Apellido</Form.Label>
                            <Form.Control type="text" name="surname" placeholder="Apellido" required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Usuario</Form.Label>
                            <Form.Control type="text" name="username" placeholder="Usuario" required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Correo Electrónico</Form.Label>
                            <Form.Control type="email" name="email" placeholder="Correo Electrónico" required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Contraseña</Form.Label>
                            <Form.Control type="password" name="password" placeholder="Contraseña" required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Confirmar Contraseña</Form.Label>
                            <Form.Control type="password" name="passwordConfirmation" placeholder="Confirmar Contraseña" required />
                        </Form.Group>
                        {error && <Alert variant="danger">{error}</Alert>}
                        <Button type="submit" variant="primary" className="w-100" disabled={isPending}>
                            {isPending ? "Registrando..." : "Registrarse"}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
}
